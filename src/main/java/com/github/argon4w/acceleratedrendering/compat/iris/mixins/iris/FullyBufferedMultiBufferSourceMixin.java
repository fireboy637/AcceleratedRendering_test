package com.github.argon4w.acceleratedrendering.compat.iris.mixins.iris;

import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.VertexConsumerExtension;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.experimental.ExtensionMethod;
import net.irisshaders.batchedentityrendering.impl.FullyBufferedMultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@ExtensionMethod(VertexConsumerExtension.class)
@Mixin(FullyBufferedMultiBufferSource.class)
public class FullyBufferedMultiBufferSourceMixin {

    @ModifyReturnValue(
            method = "getBuffer",
            at = @At("RETURN")
    )
    public VertexConsumer initAcceleration(VertexConsumer original, RenderType renderType) {
        return original
                .getHolder()
                .initAcceleration(renderType);
    }
}
