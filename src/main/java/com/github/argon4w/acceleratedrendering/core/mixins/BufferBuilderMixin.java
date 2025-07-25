package com.github.argon4w.acceleratedrendering.core.mixins;

import com.github.argon4w.acceleratedrendering.core.CoreBuffers;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.IAccelerationHolder;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.IAcceleratedVertexConsumer;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.VertexConsumerExtension;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.renderers.IAcceleratedRenderer;
import com.github.argon4w.acceleratedrendering.core.programs.ComputeShaderProgramLoader;
import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Function;
import java.util.function.Supplier;

@ExtensionMethod(VertexConsumerExtension.class)
@Mixin			(BufferBuilder			.class)
public class BufferBuilderMixin implements IAccelerationHolder, IAcceleratedVertexConsumer {

	@Unique private			RenderType								renderType		= null;
	@Unique private			Function<RenderType, VertexConsumer>	bufferSources	= renderType -> null;
	@Unique private final	Supplier<VertexConsumer>				acceleration	= Suppliers.memoize(() -> bufferSources.apply(renderType));

	@Unique
	@Override
	public VertexConsumer initAcceleration(RenderType renderType) {
		if (ComputeShaderProgramLoader.isProgramsLoaded()) {
			this.renderType		= renderType;
			this.bufferSources	= renderType.isOutline()
					? CoreBuffers.OUTLINE
					: CoreBuffers.getCoreBufferSourceSet();
		}

		return (VertexConsumer) this;
	}

	@Unique
	@Override
	public boolean isAccelerated() {
		return acceleration.get() != null;
	}

	@Unique
	@Override
	public <T> void doRender(
			IAcceleratedRenderer<T>	renderer,
			T						context,
			Matrix4f				transform,
			Matrix3f				normal,
			int						light,
			int						overlay,
			int						color
	) {
		acceleration
				.get			()
				.getAccelerated	()
				.doRender		(
						renderer,
						context,
						transform,
						normal,
						light,
						overlay,
						color
				);
	}
}
