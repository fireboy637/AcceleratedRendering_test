package com.github.argon4w.acceleratedrendering.compat.iris.mixins.vanilla;

import com.github.argon4w.acceleratedrendering.core.CoreBuffers;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
        value = LevelRenderer.class,
        priority = 999
)
public class LevelRendererMixin {

    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "CONSTANT",
                    args = "stringValue=translucent"
            )
    )
    public void drawIrisCoreBuffers(
            GraphicsResourceAllocator graphicsResourceAllocator, DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci
    ) {
        CoreBuffers.ENTITY.drawBuffers();
        CoreBuffers.BLOCK.drawBuffers();
        CoreBuffers.POS.drawBuffers();
        CoreBuffers.POS_TEX.drawBuffers();
        CoreBuffers.POS_TEX_COLOR.drawBuffers();
        CoreBuffers.POS_COLOR_TEX_LIGHT.drawBuffers();
    }

    @WrapOperation(
            method = "method_62214" /* Lambda */,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endLastBatch()V"
            )
    )
    public void preventDrawCoreBuffers(MultiBufferSource.BufferSource instance, Operation<Void> original) {
        instance.endLastBatch();
    }
}
