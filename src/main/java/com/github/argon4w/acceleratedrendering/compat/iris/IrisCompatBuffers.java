package com.github.argon4w.acceleratedrendering.compat.iris;

import com.github.argon4w.acceleratedrendering.core.buffers.AcceleratedBufferSources;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.AcceleratedBufferSource;
import com.github.argon4w.acceleratedrendering.core.buffers.environments.IBufferEnvironment;
import com.mojang.blaze3d.vertex.VertexFormat;

public class IrisCompatBuffers {

    public static final AcceleratedBufferSource BLOCK_SHADOW = new AcceleratedBufferSource(IBufferEnvironment.Presets.BLOCK);
    public static final AcceleratedBufferSource ENTITY_SHADOW = new AcceleratedBufferSource(IBufferEnvironment.Presets.ENTITY);
    public static final AcceleratedBufferSource GLYPH_SHADOW = new AcceleratedBufferSource(IBufferEnvironment.Presets.POS_COLOR_TEX_LIGHT);
    public static final AcceleratedBufferSource POS_TEX_SHADOW = new AcceleratedBufferSource(IBufferEnvironment.Presets.POS_TEX);
    public static final AcceleratedBufferSource POS_TEX_COLOR_SHADOW = new AcceleratedBufferSource(IBufferEnvironment.Presets.POS_TEX_COLOR);

    public static final AcceleratedBufferSources SHADOW = AcceleratedBufferSources
            .builder()
            .source(IrisCompatBuffers.BLOCK_SHADOW)
            .source(IrisCompatBuffers.ENTITY_SHADOW)
            .source(IrisCompatBuffers.GLYPH_SHADOW)
            .source(IrisCompatBuffers.POS_TEX_SHADOW)
            .source(IrisCompatBuffers.POS_TEX_COLOR_SHADOW)
            .mode(VertexFormat.Mode.QUADS)
            .mode(VertexFormat.Mode.TRIANGLES)
            .invalid("breeze_wind")
            .invalid("energy_swirl")
            .build();
}
