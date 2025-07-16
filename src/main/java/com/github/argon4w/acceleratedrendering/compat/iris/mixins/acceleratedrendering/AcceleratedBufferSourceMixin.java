package com.github.argon4w.acceleratedrendering.compat.iris.mixins.acceleratedrendering;

import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.AcceleratedBufferSource;
import net.irisshaders.batchedentityrendering.impl.WrappableRenderType;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = AcceleratedBufferSource.class, remap = false)
public class AcceleratedBufferSourceMixin {

    @ModifyArg(
            method = "getBuffer",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/github/argon4w/acceleratedrendering/core/buffers/accelerated/builders/AcceleratedBufferBuilder;<init>(Lcom/github/argon4w/acceleratedrendering/core/buffers/accelerated/pools/StagingBufferPool$StagingBuffer;Lcom/github/argon4w/acceleratedrendering/core/buffers/accelerated/pools/StagingBufferPool$StagingBuffer;Lcom/github/argon4w/acceleratedrendering/core/buffers/accelerated/pools/ElementBufferPool$ElementSegment;Lcom/github/argon4w/acceleratedrendering/core/buffers/accelerated/AcceleratedBufferSetPool$BufferSet;Lnet/minecraft/client/renderer/RenderType;)V"
            ),
            index = 4,
            remap = true
    )
    public RenderType unwrapIrisRenderType(RenderType renderType) {
        return renderType instanceof WrappableRenderType wrapped ? wrapped.unwrap() : renderType;
    }
}
