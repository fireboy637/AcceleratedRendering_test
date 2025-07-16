package com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders;

import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.AcceleratedBufferSetPool;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.renderers.IAcceleratedRenderer;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.renderers.IBufferDecorator;
import com.github.argon4w.acceleratedrendering.core.meshes.ServerMesh;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.nio.ByteBuffer;

public interface IAcceleratedVertexConsumer extends IBufferDecorator, IBufferGraph {

    @Override
    default VertexConsumer decorate(VertexConsumer buffer) {
        throw new UnsupportedOperationException("Unsupported Operation.");
    }

    default void beginTransform(Matrix4f transform, Matrix3f normal) {
        throw new UnsupportedOperationException("Unsupported Operation.");
    }

    default void endTransform() {
        throw new UnsupportedOperationException("Unsupported Operation.");
    }

    default boolean isAccelerated() {
        throw new UnsupportedOperationException("Unsupported Operation.");
    }

    default AcceleratedBufferSetPool.BufferSet getBufferSet() {
        throw new UnsupportedOperationException("Unsupported Operation.");
    }

    default RenderType getRenderType() {
        throw new UnsupportedOperationException("Unsupported Operation.");
    }

    default <T> void doRender(
            IAcceleratedRenderer<T> renderer,
            T context,
            Matrix4f transform,
            Matrix3f normal,
            int light,
            int overlay,
            int color
    ) {
        throw new UnsupportedOperationException("Unsupported Operation.");
    }

    default void addClientMesh(
            ByteBuffer meshBuffer,
            int size,
            int color,
            int light,
            int overlay
    ) {
        throw new UnsupportedOperationException("Unsupported Operation.");
    }

    default void addServerMesh(
            ServerMesh serverMesh,
            int color,
            int light,
            int overlay
    ) {
        throw new UnsupportedOperationException("Unsupported Operation.");
    }
}
