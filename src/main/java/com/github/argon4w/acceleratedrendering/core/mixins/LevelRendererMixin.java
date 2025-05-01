package com.github.argon4w.acceleratedrendering.core.mixins;

import com.github.argon4w.acceleratedrendering.core.CoreBuffers;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.resource.ResourceHandle;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.profiling.ProfilerFiller;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelRenderer.class, priority = 998)
public class LevelRendererMixin {
    @Inject(method = "method_62214" /* lambda */, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OutlineBufferSource;endOutlineBatch()V"))
    public void endOutlineBatches(
            FogParameters fogParameters, DeltaTracker deltaTracker, Camera camera, ProfilerFiller profilerFiller, Matrix4f matrix4f, Matrix4f matrix4f2, ResourceHandle resourceHandle, ResourceHandle resourceHandle2, ResourceHandle resourceHandle3, ResourceHandle resourceHandle4, boolean bl, Frustum frustum, ResourceHandle resourceHandle5, CallbackInfo ci
    ) {
        CoreBuffers.POS_TEX_COLOR.drawBuffers();
        CoreBuffers.POS_TEX_COLOR.clearBuffers();
    }

    @WrapOperation(method = "method_62214" /* lambda */, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endLastBatch()V"))
    public void drawCoreBuffers(MultiBufferSource.BufferSource instance, Operation<Void> original) {
        CoreBuffers.ENTITY.drawBuffers();
        CoreBuffers.BLOCK.drawBuffers();
        CoreBuffers.POS_TEX.drawBuffers();
        CoreBuffers.POS_COLOR_TEX_LIGHT.drawBuffers();

        CoreBuffers.ENTITY.clearBuffers();
        CoreBuffers.BLOCK.clearBuffers();
        CoreBuffers.POS_TEX.clearBuffers();
        CoreBuffers.POS_COLOR_TEX_LIGHT.clearBuffers();

        original.call(instance);
    }
}
