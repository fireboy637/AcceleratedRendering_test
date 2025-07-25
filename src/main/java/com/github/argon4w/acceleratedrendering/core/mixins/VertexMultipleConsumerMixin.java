package com.github.argon4w.acceleratedrendering.core.mixins;

import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.IAcceleratedVertexConsumer;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.VertexConsumerExtension;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.renderers.IAcceleratedRenderer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.experimental.ExtensionMethod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ExtensionMethod(VertexConsumerExtension.class)
@Mixin			(targets = "com.mojang.blaze3d.vertex.VertexMultiConsumer$Multiple")
public class VertexMultipleConsumerMixin implements IAcceleratedVertexConsumer {

	@Shadow @Final private	VertexConsumer[]	delegates;

	@Unique private			boolean				accelerated = true;

	@Inject(
			method	= "<init>",
			at		= @At("TAIL")
	)
	public void constructor(VertexConsumer[] delegates, CallbackInfo ci) {
		for (var delegate : delegates) {
			accelerated = accelerated && delegate
					.getAccelerated	()
					.isAccelerated	();
		}
	}

	@Unique
	@Override
	public boolean isAccelerated() {
		return accelerated;
	}

	@Unique
	@Override
	public <T>  void doRender(
			IAcceleratedRenderer<T>	renderer,
			T						context,
			Matrix4f				transform,
			Matrix3f				normal,
			int						light,
			int						overlay,
			int						color
	) {
		for (var delegate : delegates) {
			delegate
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
}
