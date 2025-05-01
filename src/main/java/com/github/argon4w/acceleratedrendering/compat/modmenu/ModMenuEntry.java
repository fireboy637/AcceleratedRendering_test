package com.github.argon4w.acceleratedrendering.compat.modmenu;

import com.github.argon4w.acceleratedrendering.configs.FeatureConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuEntry implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return FeatureConfig.CONFIG::createConfigScreen;
    }
}
