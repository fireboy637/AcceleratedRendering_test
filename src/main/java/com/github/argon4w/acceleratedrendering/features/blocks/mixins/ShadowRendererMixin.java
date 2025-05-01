package com.github.argon4w.acceleratedrendering.features.blocks.mixins;

import com.github.argon4w.acceleratedrendering.compat.iris.IShadowBufferSourceGetter;
import com.github.argon4w.acceleratedrendering.core.buffers.SimpleCrumblingBufferSource;
import com.github.argon4w.acceleratedrendering.features.blocks.AcceleratedBlockEntityRenderingFeature;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.irisshaders.iris.mixin.LevelRendererAccessor;
import net.irisshaders.iris.shadows.ShadowRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import java.util.SortedSet;

@Pseudo
@Mixin(ShadowRenderer.class)
public class ShadowRendererMixin {

    @WrapOperation(method = "renderShadows", at = @At(value = "INVOKE", target = "Lnet/irisshaders/iris/shadows/ShadowRenderingState;renderBlockEntities(Lnet/irisshaders/iris/shadows/ShadowRenderer;Lnet/minecraft/client/renderer/RenderBuffers;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/Camera;DDDFZZ)I"))
    public int wrapRenderBlockEntities(
            ShadowRenderer shadowRenderer,
            RenderBuffers renderBuffers,
            PoseStack poseStack,
            Camera camera,
            double cameraX,
            double cameraY,
            double cameraZ,
            float partialTick,
            boolean hasEntityFrustum,
            boolean lightsOnly,
            Operation<Integer> original
    ) {
        if (!AcceleratedBlockEntityRenderingFeature.isEnabled()) {
            return original.call(
                    shadowRenderer,
                    renderBuffers,
                    poseStack,
                    camera,
                    cameraX,
                    cameraY,
                    cameraZ,
                    partialTick,
                    hasEntityFrustum,
                    lightsOnly
            );
        }

        if (!AcceleratedBlockEntityRenderingFeature.shouldUseAcceleratedPipeline()) {
            return original.call(
                    shadowRenderer,
                    renderBuffers,
                    poseStack,
                    camera,
                    cameraX,
                    cameraY,
                    cameraZ,
                    partialTick,
                    hasEntityFrustum,
                    lightsOnly
            );
        }

        BlockEntityRenderDispatcher dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        Long2ObjectMap<SortedSet<BlockDestructionProgress>> blockBreakingProgressions = ((LevelRendererAccessor)Minecraft.getInstance().levelRenderer).getField_20950() /* getDestructionProgress() */;
        MutableInt counter = new MutableInt(0);

        SodiumWorldRenderer.instance().iterateVisibleBlockEntities(blockEntity -> {
            if (lightsOnly && blockEntity.getBlockState().getLightEmission() == 0) {
                return;
            }

            BlockPos pos = blockEntity.getBlockPos();
            MultiBufferSource bufferSource = ((IShadowBufferSourceGetter) shadowRenderer).getShadowBufferSource();
            SortedSet<BlockDestructionProgress> destructionProgresses = blockBreakingProgressions.get(pos.asLong());

            poseStack.pushPose();
            poseStack.translate(
                    pos.getX() - cameraX,
                    pos.getY() - cameraY,
                    pos.getZ() - cameraZ
            );

            if (destructionProgresses == null || destructionProgresses.isEmpty()) {
                dispatcher.render(
                        blockEntity,
                        partialTick,
                        poseStack,
                        bufferSource
                );

                poseStack.popPose();
                counter.increment();
                return;
            }

            int progress = destructionProgresses.last().getProgress();

            if (progress >= 0) {
                bufferSource = new SimpleCrumblingBufferSource(
                        bufferSource,
                        bufferSource.getBuffer(ModelBakery.DESTROY_TYPES.get(progress)),
                        poseStack.last(),
                        1.0f
                );
            }

            dispatcher.render(
                    blockEntity,
                    partialTick,
                    poseStack,
                    bufferSource
            );

            poseStack.popPose();
            counter.increment();
        });

        return counter.intValue();
    }
}
