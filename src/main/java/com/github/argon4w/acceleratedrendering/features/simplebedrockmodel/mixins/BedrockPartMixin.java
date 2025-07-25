package com.github.argon4w.acceleratedrendering.features.simplebedrockmodel.mixins;

import com.github.argon4w.acceleratedrendering.core.CoreFeature;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.IBufferGraph;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.VertexConsumerExtension;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.renderers.IAcceleratedRenderer;
import com.github.argon4w.acceleratedrendering.core.meshes.IMesh;
import com.github.argon4w.acceleratedrendering.core.meshes.collectors.CulledMeshCollector;
import com.github.argon4w.acceleratedrendering.features.entities.AcceleratedEntityRenderingFeature;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockCube;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.experimental.ExtensionMethod;
import net.minecraft.util.FastColor;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Pseudo
@ExtensionMethod(VertexConsumerExtension.class)
@Mixin			(BedrockPart			.class)
public class BedrockPartMixin implements IAcceleratedRenderer<Void> {

	@Unique	private static	final	PoseStack.Pose				POSE			= new PoseStack().last();
	@Unique private static	final	Vector3f[]					FIXED_NORMALS	= {
			new Vector3f(-0.0f, -1.0f, -0.0f),
			new Vector3f(+0.0f, +1.0f, +0.0f),
			new Vector3f(-0.0f, -0.0f, -1.0f),
			new Vector3f(+0.0f, +0.0f, +1.0f),
			new Vector3f(-1.0f, -0.0f, -0.0f),
			new Vector3f(+1.0f, +0.0f, +0.0f)
	};

	@Shadow @Final public			ObjectList<BedrockCube>		cubes;

	@Unique private 		final	Map<IBufferGraph, IMesh>	meshes			= new Object2ObjectOpenHashMap<>();

	@Inject(
			method		= "compile",
			at			= @At("HEAD"),
			cancellable	= true
	)
	public void compileFast(
			PoseStack.Pose	pose,
			VertexConsumer	consumer,
			int				texU,
			int				texV,
			float			red,
			float			green,
			float			blue,
			float			alpha,
			CallbackInfo	ci
	) {
		var extension = consumer.getAccelerated();

		if (		CoreFeature							.isRenderingLevel				()
				&&	AcceleratedEntityRenderingFeature	.isEnabled						()
				&&	AcceleratedEntityRenderingFeature	.shouldUseAcceleratedPipeline	()
				&&	extension							.isAccelerated					()
		) {
			ci			.cancel		();
			extension	.doRender	(
					this,
					null,
					pose.pose				(),
					pose.normal				(),
					texU,
					texV,
					FastColor.ARGB32.color	(
							(int) (alpha	* 255.0f),
							(int) (red		* 255.0f),
							(int) (green	* 255.0f),
							(int) (blue		* 255.0f)
					)
			);
		}
	}

	@Unique
	@Override
	public void render(
			VertexConsumer	vertexConsumer,
			Void			context,
			Matrix4f		transform,
			Matrix3f		normal,
			int				light,
			int				overlay,
			int				color
	) {
		var extension	= vertexConsumer.getAccelerated	();
		var mesh		= meshes		.get			(extension);

		extension.beginTransform(transform, normal);

		if (mesh != null) {
			mesh.write(
					extension,
					color,
					light,
					overlay
			);

			extension.endTransform();
			return;
		}

		var culledMeshCollector	= new CulledMeshCollector	(extension.getRenderType(), extension.getBufferSet().getBufferEnvironment().getLayout());
		var meshBuilder			= extension.decorate		(culledMeshCollector);

		for (var cube : cubes) {
			cube.compile(
					POSE,
					FIXED_NORMALS,
					meshBuilder,
					0,
					overlay,
					1.0f,
					1.0f,
					1.0f,
					1.0f
			);
		}

		culledMeshCollector.flush();

		mesh = AcceleratedEntityRenderingFeature
				.getMeshType()
				.getBuilder	()
				.build		(culledMeshCollector);

		meshes	.put	(extension, mesh);
		mesh	.write	(
				extension,
				color,
				light,
				overlay
		);

		extension.endTransform();
	}
}
