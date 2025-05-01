package com.github.argon4w.acceleratedrendering.compat.iris.mixins.acceleratedrendering;

import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.AcceleratedBufferSource;
import com.github.argon4w.acceleratedrendering.core.buffers.environments.IBufferEnvironment;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = AcceleratedBufferSource.class, remap = false)
public abstract class AcceleratedBufferSourceMixin {

    @Shadow @Final private IBufferEnvironment bufferEnvironment;

    @ModifyVariable(method = "getBuffer", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public RenderType unwrapIrisRenderType(RenderType renderType) {
        return bufferEnvironment.getRenderType(renderType);
    }
}
