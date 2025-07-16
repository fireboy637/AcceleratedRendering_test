package com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders;

import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.AcceleratedBufferSetPool;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.renderers.IAcceleratedRenderer;
import com.github.argon4w.acceleratedrendering.core.meshes.ServerMesh;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.nio.ByteBuffer;

@ExtensionMethod(VertexConsumerExtension.class)
public abstract class AcceleratedVertexConsumerWrapper implements IAcceleratedVertexConsumer, VertexConsumer {

    @Override
    public abstract VertexConsumer decorate(VertexConsumer buffer);

    protected abstract VertexConsumer getDelegate();

    @Override
    public void beginTransform(Matrix4f transform, Matrix3f normal) {
        getDelegate()
                .getAccelerated()
                .beginTransform(transform, normal);
    }

    @Override
    public void endTransform() {
        getDelegate()
                .getAccelerated()
                .endTransform();
    }

    @Override
    public boolean isAccelerated() {
        return getDelegate()
                .getAccelerated()
                .isAccelerated();
    }

    @Override
    public AcceleratedBufferSetPool.BufferSet getBufferSet() {
        return getDelegate()
                .getAccelerated()
                .getBufferSet();
    }

    @Override
    public RenderType getRenderType() {
        return getDelegate()
                .getAccelerated()
                .getRenderType();
    }

    @Override
    public void addClientMesh(
            ByteBuffer meshBuffer,
            int size,
            int color,
            int light,
            int overlay
    ) {
        getDelegate()
                .getAccelerated()
                .addClientMesh(
                        meshBuffer,
                        size,
                        color,
                        light,
                        overlay
                );
    }

    @Override
    public void addServerMesh(
            ServerMesh serverMesh,
            int color,
            int light,
            int overlay
    ) {
        getDelegate()
                .getAccelerated()
                .addServerMesh(
                        serverMesh,
                        color,
                        light,
                        overlay
                );
    }

    @Override
    public <T> void doRender(
            IAcceleratedRenderer<T> renderer,
            T context,
            Matrix4f transform,
            Matrix3f normal,
            int light,
            int overlay,
            int color
    ) {
        renderer.render(
                this,
                context,
                transform,
                normal,
                light,
                overlay,
                color
        );
    }

    @Override
    public VertexConsumer addVertex(
            float x,
            float y,
            float z
    ) {
        getDelegate().addVertex(
                x,
                y,
                z
        );
        return this;
    }

    @Override
    public VertexConsumer addVertex(
            PoseStack.Pose pose,
            float x,
            float y,
            float z
    ) {
        getDelegate().addVertex(
                pose,
                x,
                y,
                z
        );
        return this;
    }

    @Override
    public VertexConsumer setColor(
            int red,
            int green,
            int blue,
            int alpha
    ) {
        getDelegate().setColor(
                red,
                green,
                blue,
                alpha
        );
        return this;
    }

    @Override
    public VertexConsumer setUv(float u, float v) {
        getDelegate().setUv(u, v);
        return this;
    }

    @Override
    public VertexConsumer setUv1(int u, int v) {
        getDelegate().setUv1(u, v);
        return this;
    }

    @Override
    public VertexConsumer setUv2(int u, int v) {
        getDelegate().setUv2(u, v);
        return this;
    }

    @Override
    public VertexConsumer setNormal(
            float normalX,
            float normalY,
            float normalZ
    ) {
        getDelegate().setNormal(
                normalX,
                normalY,
                normalZ
        );
        return this;
    }

    @Override
    public VertexConsumer setNormal(
            PoseStack.Pose pose,
            float normalX,
            float normalY,
            float normalZ
    ) {
        getDelegate().setNormal(
                pose,
                normalX,
                normalY,
                normalZ
        );
        return this;
    }

    @Override
    public void addVertex(
            float x,
            float y,
            float z,
            int color,
            float u,
            float v,
            int packedOverlay,
            int packedLight,
            float normalX,
            float normalY,
            float normalZ
    ) {
        getDelegate().addVertex(
                x,
                y,
                z,
                color,
                u,
                v,
                packedOverlay,
                packedLight,
                normalX,
                normalY,
                normalZ
        );
    }
}
