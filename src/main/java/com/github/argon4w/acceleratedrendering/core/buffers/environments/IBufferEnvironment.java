package com.github.argon4w.acceleratedrendering.core.buffers.environments;

import com.github.argon4w.acceleratedrendering.core.backends.buffers.IServerBuffer;
import com.github.argon4w.acceleratedrendering.core.programs.ComputeShaderPrograms;
import com.github.argon4w.acceleratedrendering.core.programs.dispatchers.IPolygonProgramDispatcher;
import com.github.argon4w.acceleratedrendering.core.programs.dispatchers.TransformProgramDispatcher;
import com.github.argon4w.acceleratedrendering.core.programs.extras.IExtraVertexData;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.RenderType;

public interface IBufferEnvironment {

    void setupBufferState();

    boolean isAccelerated(VertexFormat vertexFormat);

    IExtraVertexData getExtraVertex(VertexFormat.Mode mode);

    VertexFormat getActiveFormat();

    IServerBuffer getServerMeshBuffer();

    TransformProgramDispatcher selectTransformProgramDispatcher();

    IPolygonProgramDispatcher selectCullProgramDispatcher(RenderType renderType);

    IPolygonProgramDispatcher selectProcessingProgramDispatcher(VertexFormat.Mode mode);

    RenderType getRenderType(RenderType renderType);

    int getOffset(VertexFormatElement element);

    int getVertexSize();

    class Presets {
        // Some of "final" modifiers were removed to fix crashes.

        public static final IBufferEnvironment POS_TEX_COLOR = new VanillaBufferEnvironment(DefaultVertexFormat.POSITION_TEX_COLOR, ComputeShaderPrograms.CORE_POS_TEX_COLOR_VERTEX_TRANSFORM_KEY);
        public static final IBufferEnvironment POS_TEX = new VanillaBufferEnvironment(DefaultVertexFormat.POSITION_TEX, ComputeShaderPrograms.CORE_POS_TEX_VERTEX_TRANSFORM_KEY);
        public static IBufferEnvironment BLOCK = new VanillaBufferEnvironment(DefaultVertexFormat.BLOCK, ComputeShaderPrograms.CORE_BLOCK_VERTEX_TRANSFORM_KEY);
        public static IBufferEnvironment ENTITY = new VanillaBufferEnvironment(DefaultVertexFormat.NEW_ENTITY, ComputeShaderPrograms.CORE_ENTITY_VERTEX_TRANSFORM_KEY);
        public static IBufferEnvironment POS_COLOR_TEX_LIGHT = new VanillaBufferEnvironment(DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, ComputeShaderPrograms.CORE_POS_COLOR_TEX_LIGHT_VERTEX_TRANSFORM_KEY);
    }
}
