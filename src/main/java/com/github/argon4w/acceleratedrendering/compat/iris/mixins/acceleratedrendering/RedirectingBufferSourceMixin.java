package com.github.argon4w.acceleratedrendering.compat.iris.mixins.acceleratedrendering;

import com.github.argon4w.acceleratedrendering.compat.iris.IAcceleratedUnwrap;
import com.github.argon4w.acceleratedrendering.compat.iris.IrisCompatFeature;
import com.github.argon4w.acceleratedrendering.core.buffers.RedirectingBufferSource;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.irisshaders.batchedentityrendering.impl.WrappableRenderType;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RedirectingBufferSource.class)
public class RedirectingBufferSourceMixin {

    @WrapOperation(method = "getBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;toString()Ljava/lang/String;"))
    public String unwrapIrisRenderType(RenderType instance, Operation<String> original) {
        IAcceleratedUnwrap fast = (IAcceleratedUnwrap) instance;

        if (IrisCompatFeature.isFastRenderTypeCheckEnabled()) {
            return original.call(fast.unwrapFast());
        }

        if (instance instanceof WrappableRenderType wrapped) {
            return original.call(wrapped.unwrap());
        }

        if (fast.isAccelerated()) {
            return original.call(fast.unwrapFast());
        }

        return original.call(instance);
    }
}
