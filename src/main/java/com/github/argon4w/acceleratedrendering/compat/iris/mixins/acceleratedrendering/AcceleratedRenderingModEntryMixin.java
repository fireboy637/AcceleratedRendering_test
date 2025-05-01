package com.github.argon4w.acceleratedrendering.compat.iris.mixins.acceleratedrendering;

import com.github.argon4w.acceleratedrendering.AcceleratedRenderingModEntry;
import com.github.argon4w.acceleratedrendering.compat.iris.programs.IrisPrograms;
import com.llamalad7.mixinextras.sugar.Local;
import net.neoforged.bus.api.IEventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AcceleratedRenderingModEntry.class, remap = false)
public class AcceleratedRenderingModEntryMixin {

    @Inject(method = "onInitializeClient", at = @At("TAIL"))
    public void registerIrisEvents(
            CallbackInfo ci,
            @Local IEventBus eventBus
    ) {
        eventBus.register(IrisPrograms.class);
    }
}
