package com.github.argon4w.acceleratedrendering.features.blocks.mixins;

import com.github.argon4w.acceleratedrendering.core.CoreBuffers;
import com.github.argon4w.acceleratedrendering.core.buffers.SimpleCrumblingBufferSource;
import com.github.argon4w.acceleratedrendering.features.blocks.AcceleratedBlockEntityRenderingFeature;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.SortedSet;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow @Final private Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress;
//    @Shadow @Final private Minecraft minecraft;

    @WrapOperation(method = "renderBlockEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;render(Lnet/minecraft/world/level/block/entity/BlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V"))
    public void wrapRenderBlockEntity(
            BlockEntityRenderDispatcher instance,
            BlockEntity pBlockEntity,
            float pPartialTick,
            PoseStack pPoseStack,
            MultiBufferSource pBufferSource,
            Operation<Void> original
    ) {
        if (!AcceleratedBlockEntityRenderingFeature.isEnabled()) {
            original.call(
                    instance,
                    pBlockEntity,
                    pPartialTick,
                    pPoseStack,
                    pBufferSource
            );
            return;
        }

        if (!AcceleratedBlockEntityRenderingFeature.shouldUseAcceleratedPipeline()) {
            original.call(
                    instance,
                    pBlockEntity,
                    pPartialTick,
                    pPoseStack,
                    pBufferSource
            );
            return;
        }

//        if (shouldShowEntityOutlines() && pBlockEntity.hasCustomOutlineRendering(minecraft.player)) {
//            flag2.set(true);
//        }

        BlockPos blockPos = pBlockEntity.getBlockPos();
        MultiBufferSource bufferSource = CoreBuffers.CORE;
        SortedSet<BlockDestructionProgress> destructionProgresses = destructionProgress.get(blockPos.asLong());

        if (destructionProgresses == null || destructionProgresses.isEmpty()) {
            original.call(
                    instance,
                    pBlockEntity,
                    pPartialTick,
                    pPoseStack,
                    bufferSource
            );
            return;
        }

        int progress = destructionProgresses.last().getProgress();

        if (progress >= 0) {
            bufferSource = new SimpleCrumblingBufferSource(
                    bufferSource,
                    bufferSource.getBuffer(ModelBakery.DESTROY_TYPES.get(progress)),
                    pPoseStack.last(),
                    1.0f
            );
        }

        original.call(
                instance,
                pBlockEntity,
                pPartialTick,
                pPoseStack,
                bufferSource
        );
    }
}
