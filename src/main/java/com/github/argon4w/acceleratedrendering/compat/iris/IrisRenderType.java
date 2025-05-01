package com.github.argon4w.acceleratedrendering.compat.iris;

import com.github.argon4w.acceleratedrendering.core.utils.VertexFormatUtils;
import com.google.common.base.Objects;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.irisshaders.batchedentityrendering.impl.WrappableRenderType;
import net.minecraft.client.renderer.RenderType;

import java.util.Optional;

public class IrisRenderType extends RenderType implements WrappableRenderType, IAcceleratedUnwrap {

    private final RenderType renderType;
    private final VertexFormat vertexFormat;
    private final int hashCode;

    public IrisRenderType(RenderType renderType, VertexFormat vertexFormat) {
        super(
                renderType.toString(),
                vertexFormat,
                renderType.mode,
                renderType.bufferSize,
                renderType.affectsCrumbling,
                renderType.sortOnUpload,
                renderType::setupRenderState,
                renderType::clearRenderState
        );

        this.renderType = renderType;
        this.vertexFormat = vertexFormat;
        this.hashCode = Objects.hashCode(renderType, VertexFormatUtils.hashCodeFast(vertexFormat));
    }

    @Override
    public Optional<RenderType> outline() {
        return renderType.outline();
    }

    @Override
    public boolean isOutline() {
        return renderType.isOutline();
    }

    @Override
    public RenderType unwrap() {
        return renderType;
    }

    @Override
    public RenderType unwrapFast() {
        return renderType;
    }

    @Override
    public boolean isAccelerated() {
        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        IrisRenderType that = (IrisRenderType) o;

        return Objects.equal(renderType, that.renderType)
                && Objects.equal(vertexFormat, that.vertexFormat);
    }
}
