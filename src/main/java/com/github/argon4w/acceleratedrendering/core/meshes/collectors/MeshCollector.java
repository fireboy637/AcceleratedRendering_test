package com.github.argon4w.acceleratedrendering.core.meshes.collectors;

import com.github.argon4w.acceleratedrendering.core.utils.MemUtils;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.util.ARGB;
import org.lwjgl.system.MemoryUtil;

public class MeshCollector implements VertexConsumer {

    private final VertexFormat vertexFormat;;
    private final ByteBufferBuilder buffer;

    private final int vertexSize;
    private final long posOffset;
    private final long colorOffset;
    private final long uv0Offset;
    private final long uv2Offset;
    private final long normalOffset;

    private long vertexAddress;
    private int vertexCount;

    public MeshCollector(VertexFormat vertexFormat) {
        this.vertexFormat = vertexFormat;
        this.buffer = new ByteBufferBuilder(1024);

        this.vertexSize = this.vertexFormat.getVertexSize();
        this.colorOffset = this.vertexFormat.getOffset(VertexFormatElement.COLOR);
        this.posOffset = this.vertexFormat.getOffset(VertexFormatElement.POSITION);
        this.uv0Offset = this.vertexFormat.getOffset(VertexFormatElement.UV);
        this.uv2Offset = this.vertexFormat.getOffset(VertexFormatElement.UV2);
        this.normalOffset = this.vertexFormat.getOffset(VertexFormatElement.NORMAL);

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
        vertexAddress = buffer.reserve(vertexSize);

        MemoryUtil.memPutFloat(vertexAddress + posOffset + 0L, pX);
        MemoryUtil.memPutFloat(vertexAddress + posOffset + 4L, pY);
        MemoryUtil.memPutFloat(vertexAddress + posOffset + 8L, pZ);

        return this;
    }

    @Override
    public VertexConsumer setColor(
            int pRed,
            int pGreen,
            int pBlue,
            int pAlpha
    ) {
        if (colorOffset == -1) {
            return this;
        }

        if (vertexAddress == -1) {
            throw new IllegalStateException("Vertex not building!");
        }

        MemoryUtil.memPutByte(vertexAddress + colorOffset + 0L, (byte) pRed);
        MemoryUtil.memPutByte(vertexAddress + colorOffset + 1L, (byte) pGreen);
        MemoryUtil.memPutByte(vertexAddress + colorOffset + 2L, (byte) pBlue);
        MemoryUtil.memPutByte(vertexAddress + colorOffset + 3L, (byte) pAlpha);

        return this;
    }

    @Override
    public VertexConsumer setUv(float pU, float pV) {
        if (uv0Offset == -1) {
            return this;
        }

        if (vertexAddress == -1) {
            throw new IllegalStateException("Vertex not building!");
        }

        MemoryUtil.memPutFloat(vertexAddress + uv0Offset + 0L, pU);
        MemoryUtil.memPutFloat(vertexAddress + uv0Offset + 4L, pV);

        return this;
    }

    @Override
    public VertexConsumer setUv1(int pU, int pV) {
        return this;
    }

    @Override
    public VertexConsumer setUv2(int pU, int pV) {
        if (uv2Offset == -1) {
            return this;
        }

        if (vertexAddress == -1) {
            throw new IllegalStateException("Vertex not building!");
        }

        MemoryUtil.memPutShort(vertexAddress + uv2Offset + 0L, (short) pU);
        MemoryUtil.memPutShort(vertexAddress + uv2Offset + 2L, (short) pV);

        return this;
    }

    @Override
    public VertexConsumer setNormal(
            float pNormalX,
            float pNormalY,
            float pNormalZ
    ) {
        if (normalOffset == -1) {
            return this;
        }

        if (vertexAddress == -1) {
            throw new IllegalStateException("Vertex not building!");
        }

        MemUtils.putNormal(vertexAddress + normalOffset + 0L, pNormalX);
        MemUtils.putNormal(vertexAddress + normalOffset + 1L, pNormalY);
        MemUtils.putNormal(vertexAddress + normalOffset + 2L, pNormalZ);

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
        vertexAddress = buffer.reserve(vertexSize);

        MemoryUtil.memPutFloat(vertexAddress + posOffset + 0L, pX);
        MemoryUtil.memPutFloat(vertexAddress + posOffset + 4L, pY);
        MemoryUtil.memPutFloat(vertexAddress + posOffset + 8L, pZ);

        if (colorOffset != -1L) {
            MemoryUtil.memPutInt(vertexAddress + colorOffset + 0L, ARGB.toABGR(pColor));
        }

        if (uv0Offset != -1L) {
            MemoryUtil.memPutFloat(vertexAddress + uv0Offset + 0L, pU);
            MemoryUtil.memPutFloat(vertexAddress + uv0Offset + 4L, pV);
        }

        if (uv2Offset != -1L) {
            MemoryUtil.memPutInt(vertexAddress + uv2Offset + 0L, pPackedLight);
        }

        if (normalOffset != -1L) {
            MemUtils.putNormal(vertexAddress + normalOffset + 0L, pNormalX);
            MemUtils.putNormal(vertexAddress + normalOffset + 1L, pNormalY);
            MemUtils.putNormal(vertexAddress + normalOffset + 2L, pNormalZ);
        }
    }

    public ByteBufferBuilder getBuffer() {
        return buffer;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public VertexFormat getVertexFormat() {
        return vertexFormat;
    }
}
