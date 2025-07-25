package com.github.argon4w.acceleratedrendering.features.modelparts.mixins;

import com.github.argon4w.acceleratedrendering.core.CoreFeature;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.IBufferGraph;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.VertexConsumerExtension;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.renderers.IAcceleratedRenderer;
import com.github.argon4w.acceleratedrendering.core.meshes.IMesh;
import com.github.argon4w.acceleratedrendering.core.meshes.collectors.CulledMeshCollector;
import com.github.argon4w.acceleratedrendering.features.entities.AcceleratedEntityRenderingFeature;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.model.geom.ModelPart;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@ExtensionMethod(VertexConsumerExtension.class)
@Mixin			(ModelPart				.class)
public class ModelPartMixin implements IAcceleratedRenderer<Void> {

	@Shadow @Final private	List<ModelPart.Cube>		cubes;

	@Unique private final	Map<IBufferGraph, IMesh>	meshes = new Object2ObjectOpenHashMap<>();

	@Inject(
			method		= "compile",
			at			= @At("HEAD"),
			cancellable	= true
	)
	public void compileFast(
			PoseStack.Pose	pPose,
			VertexConsumer	pBuffer,
			int				pPackedLight,
			int				pPackedOverlay,
			int				pColor,
			CallbackInfo	ci
	) {
		var extension = pBuffer.getAccelerated();

		if (		CoreFeature							.isRenderingLevel				()
				&&	AcceleratedEntityRenderingFeature	.isEnabled						()
				&&	AcceleratedEntityRenderingFeature	.shouldUseAcceleratedPipeline	()
				&&	extension							.isAccelerated					()
		) {
			ci			.cancel		();
			extension	.doRender	(
					this,
					null,
					pPose.pose	(),
					pPose.normal(),
					pPackedLight,
					pPackedOverlay,
					pColor
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
			for (var polygon : cube.polygons) {
				var polygonNormal = polygon.normal;

				for (var vertex : polygon.vertices) {
					var vertexPosition = vertex.pos;

					meshBuilder.addVertex(
							vertexPosition.x / 16.0f,
							vertexPosition.y / 16.0f,
							vertexPosition.z / 16.0f,
							-1,
							vertex.u,
							vertex.v,
							overlay,
							0,
							polygonNormal.x,
							polygonNormal.y,
							polygonNormal.z
					);
				}
			}
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
