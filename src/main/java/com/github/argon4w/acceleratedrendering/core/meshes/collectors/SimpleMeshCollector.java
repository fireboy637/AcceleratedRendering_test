package com.github.argon4w.acceleratedrendering.core.meshes.collectors;

import com.github.argon4w.acceleratedrendering.core.buffers.memory.IMemoryInterface;
import com.github.argon4w.acceleratedrendering.core.buffers.memory.IMemoryLayout;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.util.ARGB;

public class SimpleMeshCollector implements VertexConsumer, IMeshCollector {

    private final IMemoryLayout<VertexFormatElement> layout;
    private final ByteBufferBuilder buffer;

    private final long vertexSize;
    private final IMemoryInterface posOffset;
    private final IMemoryInterface colorOffset;
    private final IMemoryInterface uv0Offset;
    private final IMemoryInterface uv2Offset;
    private final IMemoryInterface normalOffset;

    private long vertexAddress;
    private int vertexCount;

    public SimpleMeshCollector(IMemoryLayout<VertexFormatElement> layout) {
        this.layout = layout;
        this.buffer = new ByteBufferBuilder(1024);

        this.vertexSize = this.layout.getSize();
        this.posOffset = this.layout.getElement(VertexFormatElement.POSITION);
        this.colorOffset = this.layout.getElement(VertexFormatElement.COLOR);
        this.uv0Offset = this.layout.getElement(VertexFormatElement.UV);
        this.uv2Offset = this.layout.getElement(VertexFormatElement.UV2);
        this.normalOffset = this.layout.getElement(VertexFormatElement.NORMAL);

        this.vertexAddress = -1L;
        this.vertexCount = 0;
    }

    @Override
    public VertexConsumer addVertex(
            float pX,
            float pY,
            float pZ
    ) {
        vertexCount++;
        vertexAddress = buffer.reserve((int) vertexSize);

        posOffset.putFloat(vertexAddress + 0L, pX);
        posOffset.putFloat(vertexAddress + 4L, pY);
        posOffset.putFloat(vertexAddress + 8L, pZ);

        return this;
    }

    @Override
    public VertexConsumer setColor(
            int pRed,
            int pGreen,
            int pBlue,
            int pAlpha
    ) {
        if (vertexAddress == -1) {
            throw new IllegalStateException("Vertex not building!");
        }

        colorOffset.putByte(vertexAddress + 0L, (byte) pRed);
        colorOffset.putByte(vertexAddress + 1L, (byte) pGreen);
        colorOffset.putByte(vertexAddress + 2L, (byte) pBlue);
        colorOffset.putByte(vertexAddress + 3L, (byte) pAlpha);

        return this;
    }

    @Override
    public VertexConsumer setUv(float pU, float pV) {
        if (vertexAddress == -1) {
            throw new IllegalStateException("Vertex not building!");
        }

        uv0Offset.putFloat(vertexAddress + 0L, pU);
        uv0Offset.putFloat(vertexAddress + 4L, pV);

        return this;
    }

    @Override
    public VertexConsumer setUv1(int pU, int pV) {
        return this;
    }

    @Override
    public VertexConsumer setUv2(int pU, int pV) {
        if (vertexAddress == -1) {
            throw new IllegalStateException("Vertex not building!");
        }

        uv2Offset.putShort(vertexAddress + 0L, (short) pU);
        uv2Offset.putShort(vertexAddress + 2L, (short) pV);

        return this;
    }

    @Override
    public VertexConsumer setNormal(
            float pNormalX,
            float pNormalY,
            float pNormalZ
    ) {
        if (vertexAddress == -1) {
            throw new IllegalStateException("Vertex not building!");
        }

        normalOffset.putNormal(vertexAddress + 0L, pNormalX);
        normalOffset.putNormal(vertexAddress + 1L, pNormalY);
        normalOffset.putNormal(vertexAddress + 2L, pNormalZ);

        return this;
    }

    @Override
    public void addVertex(
            float pX,
            float pY,
            float pZ,
            int pColor,
            float pU,
            float pV,
            int pPackedOverlay,
            int pPackedLight,
            float pNormalX,
            float pNormalY,
            float pNormalZ
    ) {
        vertexCount++;
        vertexAddress = buffer.reserve((int) vertexSize);

        posOffset.putFloat(vertexAddress + 0L, pX);
        posOffset.putFloat(vertexAddress + 4L, pY);
        posOffset.putFloat(vertexAddress + 8L, pZ);
        colorOffset.putInt(vertexAddress, ARGB.toABGR(pColor));
        uv0Offset.putFloat(vertexAddress + 0L, pU);
        uv0Offset.putFloat(vertexAddress + 4L, pV);
        uv2Offset.putInt(vertexAddress, pPackedLight);
        normalOffset.putNormal(vertexAddress + 0L, pNormalX);
        normalOffset.putNormal(vertexAddress + 1L, pNormalY);
        normalOffset.putNormal(vertexAddress + 2L, pNormalZ);
    }

    @Override
    public ByteBufferBuilder getBuffer() {
        return buffer;
    }

    @Override
    public IMemoryLayout<VertexFormatElement> getLayout() {
        return layout;
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }
}
