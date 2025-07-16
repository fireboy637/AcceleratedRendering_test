package com.github.argon4w.acceleratedrendering.configs;

import cn.xiaym.unifiedconf.EnumReference;
import cn.xiaym.unifiedconf.IntReference;
import com.github.argon4w.acceleratedrendering.core.meshes.MeshType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class FeatureConfig {
    public static final FeatureConfig CONFIG = new FeatureConfig();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir()
            .resolve("accelerated-rendering.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public final IntReference corePooledBufferSetSize = new IntReference(5);
    public final IntReference corePooledElementBufferSize = new IntReference(32);
    public final IntReference coreCachedImageSize = new IntReference(32);
    public final EnumReference<FeatureStatus> coreDebugContextEnabled = new EnumReference<>(FeatureStatus.ENABLED);
    public final EnumReference<FeatureStatus> coreForceTranslucentAcceleration = new EnumReference<>(FeatureStatus.DISABLED);
    public final EnumReference<FeatureStatus> coreCacheIdenticalPose = new EnumReference<>(FeatureStatus.ENABLED);

    public final EnumReference<FeatureStatus> acceleratedEntityRenderingFeatureStatus = new EnumReference<>(FeatureStatus.ENABLED);
    public final EnumReference<PipelineSetting> acceleratedEntityRenderingDefaultPipeline = new EnumReference<>(PipelineSetting.ACCELERATED);
    public final EnumReference<MeshType> acceleratedEntityRenderingMeshType = new EnumReference<>(MeshType.SERVER);

    public final EnumReference<FeatureStatus> acceleratedTextRenderingFeatureStatus = new EnumReference<>(FeatureStatus.ENABLED);
    public final EnumReference<PipelineSetting> acceleratedTextRenderingDefaultPipeline = new EnumReference<>(PipelineSetting.ACCELERATED);
    public final EnumReference<MeshType> acceleratedTextRenderingMeshType = new EnumReference<>(MeshType.SERVER);

    public final EnumReference<FeatureStatus> orientationCullingFeatureStatus = new EnumReference<>(FeatureStatus.ENABLED);
    public final EnumReference<FeatureStatus> orientationCullingDefaultCulling = new EnumReference<>(FeatureStatus.ENABLED);
    public final EnumReference<FeatureStatus> orientationCullingIgnoreCullState = new EnumReference<>(FeatureStatus.DISABLED);

    public final EnumReference<FeatureStatus> irisCompatFeatureStatus = new EnumReference<>(FeatureStatus.ENABLED);
    public final EnumReference<FeatureStatus> irisCompatOrientationCullingCompat = new EnumReference<>(FeatureStatus.ENABLED);
    public final EnumReference<FeatureStatus> irisCompatShadowCulling = new EnumReference<>(FeatureStatus.ENABLED);
    public final EnumReference<FeatureStatus> irisCompatPolygonProcessing = new EnumReference<>(FeatureStatus.ENABLED);

    // Welcome to hell
    public Screen createConfigScreen(Screen parent) {
        FeatureConfig defaultConfig = new FeatureConfig();

        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent)
                .setTitle(Component.translatable("acceleratedrendering.configuration.title"))
                .setSavingRunnable(this::saveConfig);

        // Core Settings
        builder.getOrCreateCategory(Component.translatable("acceleratedrendering.configuration.core_settings"))
                .addEntry(builder.entryBuilder()
                        .startIntField(Component.translatable("acceleratedrendering.configuration.core_settings.pooled_buffer_set_size"), corePooledBufferSetSize.getAsInt())
                        .requireRestart()
                        .setDefaultValue(defaultConfig.corePooledBufferSetSize.getAsInt())
                        .setMin(1)
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.core_settings.pooled_buffer_set_size.tooltip"))
                        .setSaveConsumer(corePooledBufferSetSize::set)
                        .build())
                .addEntry(builder.entryBuilder()
                        .startIntField(Component.translatable("acceleratedrendering.configuration.core_settings.pooled_element_buffer_size"), corePooledElementBufferSize.getAsInt())
                        .requireRestart()
                        .setDefaultValue(defaultConfig.corePooledElementBufferSize.getAsInt())
                        .setMin(1)
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.core_settings.pooled_element_buffer_size.tooltip"))
                        .setSaveConsumer(corePooledElementBufferSize::set)
                        .build())
                .addEntry(builder.entryBuilder()
                        .startIntField(Component.translatable("acceleratedrendering.configuration.core_settings.cached_image_size"), coreCachedImageSize.getAsInt())
                        .setDefaultValue(defaultConfig.coreCachedImageSize.getAsInt())
                        .setMin(1)
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.core_settings.cached_image_size.tooltip"))
                        .setSaveConsumer(coreCachedImageSize::set)
                        .build())
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.core_settings.debug_context"), FeatureStatus.class, coreDebugContextEnabled.get())
                        .requireRestart()
                        .setDefaultValue(defaultConfig.coreDebugContextEnabled.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.core_settings.debug_context.tooltip"))
                        .setSaveConsumer(coreDebugContextEnabled::set)
                        .build()
                )
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.core_settings.force_translucent_acceleration"), FeatureStatus.class, coreForceTranslucentAcceleration.get())
                        .setDefaultValue(defaultConfig.coreForceTranslucentAcceleration.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.core_settings.force_translucent_acceleration.tooltip"))
                        .setSaveConsumer(coreForceTranslucentAcceleration::set)
                        .build())
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.core_settings.cache_identical_pose"), FeatureStatus.class, coreCacheIdenticalPose.get())
                        .setDefaultValue(defaultConfig.coreCacheIdenticalPose.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.core_settings.cache_identical_pose.tooltip"))
                        .setSaveConsumer(coreCacheIdenticalPose::set)
                        .build());

        // Accelerated Entity Rendering Settings
        builder.getOrCreateCategory(Component.translatable("acceleratedrendering.configuration.accelerated_entity_rendering"))
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.accelerated_entity_rendering.feature_status"), FeatureStatus.class, acceleratedEntityRenderingFeatureStatus.get())
                        .setDefaultValue(defaultConfig.acceleratedEntityRenderingFeatureStatus.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.accelerated_entity_rendering.feature_status.tooltip"))
                        .setSaveConsumer(acceleratedEntityRenderingFeatureStatus::set)
                        .build())
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.accelerated_entity_rendering.default_pipeline"), PipelineSetting.class, acceleratedEntityRenderingDefaultPipeline.get())
                        .setDefaultValue(defaultConfig.acceleratedEntityRenderingDefaultPipeline.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.accelerated_entity_rendering.default_pipeline.tooltip"))
                        .setSaveConsumer(acceleratedEntityRenderingDefaultPipeline::set)
                        .build())
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.accelerated_entity_rendering.mesh_type"), MeshType.class, acceleratedEntityRenderingMeshType.get())
                        .requireRestart()
                        .setDefaultValue(defaultConfig.acceleratedEntityRenderingMeshType.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.accelerated_entity_rendering.mesh_type.tooltip"))
                        .setSaveConsumer(acceleratedEntityRenderingMeshType::set)
                        .build());

        // Accelerated Text Rendering Settings
        builder.getOrCreateCategory(Component.translatable("acceleratedrendering.configuration.accelerated_text_rendering"))
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.accelerated_text_rendering.feature_status"), FeatureStatus.class, acceleratedTextRenderingFeatureStatus.get())
                        .setDefaultValue(defaultConfig.acceleratedTextRenderingFeatureStatus.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.accelerated_text_rendering.feature_status.tooltip"))
                        .setSaveConsumer(acceleratedTextRenderingFeatureStatus::set)
                        .build())
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.accelerated_text_rendering.default_pipeline"), PipelineSetting.class, acceleratedTextRenderingDefaultPipeline.get())
                        .setDefaultValue(defaultConfig.acceleratedTextRenderingDefaultPipeline.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.accelerated_text_rendering.default_pipeline.tooltip"))
                        .setSaveConsumer(acceleratedTextRenderingDefaultPipeline::set)
                        .build())
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.accelerated_text_rendering.mesh_type"), MeshType.class, acceleratedTextRenderingMeshType.get())
                        .requireRestart()
                        .setDefaultValue(defaultConfig.acceleratedTextRenderingMeshType.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.accelerated_text_rendering.mesh_type.tooltip"))
                        .setSaveConsumer(acceleratedTextRenderingMeshType::set)
                        .build());

        // Simple Orientation Face Culling Settings
        builder.getOrCreateCategory(Component.translatable("acceleratedrendering.configuration.orientation_culling"))
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.orientation_culling.feature_status"), FeatureStatus.class, orientationCullingFeatureStatus.get())
                        .setDefaultValue(defaultConfig.orientationCullingFeatureStatus.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.orientation_culling.feature_status.tooltip"))
                        .setSaveConsumer(orientationCullingFeatureStatus::set)
                        .build())
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.orientation_culling.default_culling"), FeatureStatus.class, orientationCullingDefaultCulling.get())
                        .setDefaultValue(defaultConfig.orientationCullingDefaultCulling.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.orientation_culling.default_culling.tooltip"))
                        .setSaveConsumer(orientationCullingDefaultCulling::set)
                        .build())
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.orientation_culling.ignore_cull_state"), FeatureStatus.class, orientationCullingIgnoreCullState.get())
                        .setDefaultValue(defaultConfig.orientationCullingIgnoreCullState.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.orientation_culling.ignore_cull_state.tooltip"))
                        .setSaveConsumer(orientationCullingIgnoreCullState::set)
                        .build());

        // Iris Compatibility Settings
        builder.getOrCreateCategory(Component.translatable("acceleratedrendering.configuration.iris_compatibility"))
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.iris_compatibility.feature_status"), FeatureStatus.class, irisCompatFeatureStatus.get())
                        .setDefaultValue(defaultConfig.irisCompatFeatureStatus.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.iris_compatibility.feature_status.tooltip"))
                        .setSaveConsumer(irisCompatFeatureStatus::set)
                        .build())
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.iris_compatibility.orientation_culling_compatibility"), FeatureStatus.class, irisCompatOrientationCullingCompat.get())
                        .setDefaultValue(defaultConfig.irisCompatOrientationCullingCompat.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.iris_compatibility.orientation_culling_compatibility.tooltip"))
                        .setSaveConsumer(irisCompatOrientationCullingCompat::set)
                        .build())
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.iris_compatibility.shadow_culling"), FeatureStatus.class, irisCompatShadowCulling.get())
                        .setDefaultValue(defaultConfig.irisCompatShadowCulling.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.iris_compatibility.shadow_culling.tooltip"))
                        .setSaveConsumer(irisCompatShadowCulling::set)
                        .build())
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Component.translatable("acceleratedrendering.configuration.iris_compatibility.polygon_processing"), FeatureStatus.class, irisCompatPolygonProcessing.get())
                        .setDefaultValue(defaultConfig.irisCompatPolygonProcessing.get())
                        .setTooltip(Component.translatable("acceleratedrendering.configuration.iris_compatibility.polygon_processing.tooltip"))
                        .setSaveConsumer(irisCompatPolygonProcessing::set)
                        .build());

        return builder.build();
    }

    public void loadConfig() {
        if (Files.notExists(CONFIG_PATH)) {
            saveConfig();
            return;
        }

        try {
            JsonObject object = GSON.fromJson(Files.readString(CONFIG_PATH), JsonObject.class);

            Arrays.stream(FeatureConfig.class.getFields()).filter(it -> {
                Class<?> type = it.getType();
                return object.has(it.getName()) && type == IntReference.class || type == EnumReference.class;
            }).forEach(it -> {
                try {
                    JsonElement element = object.get(it.getName());
                    Object fieldValue = it.get(this);
                    if (fieldValue instanceof IntReference intReference) {
                        intReference.set(element.getAsInt());
                    } else if (fieldValue instanceof EnumReference<?>) {
                        String typeName = it.getGenericType().getTypeName();
                        Class<?> enumClass = Class.forName(typeName.substring(typeName.indexOf("<") + 1, typeName.indexOf(">")));
                        Enum<?> enumValue = (Enum<?>) enumClass.getMethod("valueOf", String.class)
                                .invoke(null, element.getAsString());
                        EnumReference.class.getMethod("set", Enum.class).invoke(fieldValue, enumValue);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            LoggerFactory.getLogger("AcceleratedRendering")
                    .error("Failed to load config, using default settings instead.", e instanceof RuntimeException ? e.getCause() : e);
        }
    }

    public void saveConfig() {
        JsonObject object = new JsonObject();

        Arrays.stream(FeatureConfig.class.getFields()).filter(it -> {
            Class<?> type = it.getType();
            return type == IntReference.class || type == EnumReference.class;
        }).forEach(it -> {
            try {
                Object value = it.get(this);
                if (value instanceof IntReference) {
                    object.addProperty(it.getName(), ((IntReference) value).getAsInt());
                } else if (value instanceof EnumReference) {
                    object.addProperty(it.getName(), ((EnumReference<?>) value).get().name());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
