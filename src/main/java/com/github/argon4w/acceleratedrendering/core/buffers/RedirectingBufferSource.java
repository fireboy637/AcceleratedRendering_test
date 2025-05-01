package com.github.argon4w.acceleratedrendering.core.buffers;

import com.github.argon4w.acceleratedrendering.core.CoreFeature;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.IAcceleratedBufferSource;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import java.util.Set;

public class RedirectingBufferSource extends MultiBufferSource.BufferSource {

    private final Set<IAcceleratedBufferSource> bufferSources;
    private final Set<VertexFormat.Mode> availableModes;
    private final Set<String> fallbackNames;
    private final MultiBufferSource fallbackSource;
    private final boolean supportSorting;

    private RedirectingBufferSource(
            Set<IAcceleratedBufferSource> bufferSources,
            Set<VertexFormat.Mode> availableModes,
            Set<String> fallbackNames,
            MultiBufferSource fallbackSource,
            boolean supportSorting
    ) {
        super(null, null);

        this.bufferSources = bufferSources;
        this.availableModes = availableModes;
        this.fallbackNames = fallbackNames;
        this.fallbackSource = fallbackSource;
        this.supportSorting = supportSorting;
    }

    @Override
    public void endBatch(RenderType pRenderType) {

    }

    @Override
    public void endBatch() {

    }

    @Override
    public void endLastBatch() {

    }

    @Override
    public VertexConsumer getBuffer(RenderType pRenderType) {
        if (!CoreFeature.shouldForceAccelerateTranslucent() && pRenderType.sortOnUpload && !supportSorting) {
            return fallbackSource.getBuffer(pRenderType);
        }

        if (!availableModes.contains(pRenderType.mode)) {
            return fallbackSource.getBuffer(pRenderType);
        }

        if (fallbackNames.contains(pRenderType.toString())) {
            return fallbackSource.getBuffer(pRenderType);
        }

        for (IAcceleratedBufferSource bufferSource : bufferSources) {
            if (bufferSource.isAccelerated(pRenderType)) {
                return bufferSource.getBuffer(pRenderType);
            }
        }

        return fallbackSource.getBuffer(pRenderType);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Set<IAcceleratedBufferSource> bufferSources;
        private final Set<VertexFormat.Mode> availableModes;
        private final Set<String> fallbackNames;

        private boolean supportSorting;
        private MultiBufferSource fallbackSource;

        private Builder() {
            this.bufferSources = new ObjectArraySet<>();
            this.availableModes = new ReferenceOpenHashSet<>();
            this.fallbackNames = new ObjectOpenHashSet<>();

            this.supportSorting = false;
            this.fallbackSource = null;
        }

        public Builder fallback(MultiBufferSource bufferSource) {
            fallbackSource = bufferSource;
            return this;
        }

        public Builder bufferSource(IAcceleratedBufferSource bufferSource) {
            bufferSources.add(bufferSource);
            return this;
        }

        public Builder mode(VertexFormat.Mode mode) {
            availableModes.add(mode);
            return this;
        }

        public Builder fallbackName(String name) {
            fallbackNames.add(name);
            return this;
        }

        public Builder supportSort() {
            supportSorting = true;
            return this;
        }

        public RedirectingBufferSource build() {
            return new RedirectingBufferSource(
                    bufferSources,
                    availableModes,
                    fallbackNames,
                    fallbackSource,
                    supportSorting
            );
        }
    }
}
