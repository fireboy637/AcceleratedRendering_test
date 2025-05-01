package com.github.argon4w.acceleratedrendering;

import com.github.argon4w.acceleratedrendering.configs.FeatureConfig;
import com.github.argon4w.acceleratedrendering.core.programs.ComputeShaderPrograms;
import com.github.argon4w.acceleratedrendering.features.culling.OrientationCullingPrograms;
import net.fabricmc.api.ClientModInitializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoader;

public class AcceleratedRenderingModEntry implements ClientModInitializer {
    public static final String MOD_ID = "acceleratedrendering";

    @Override
    public void onInitializeClient() {
        FeatureConfig.CONFIG.loadConfig();

        ModContainer container = ModLoader.createModContainer(MOD_ID);
        IEventBus eventBus = container.getModEventBus();
        eventBus.register(ComputeShaderPrograms.class);
        eventBus.register(OrientationCullingPrograms.class);
    }
}
