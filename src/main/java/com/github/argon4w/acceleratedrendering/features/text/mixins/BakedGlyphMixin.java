package com.github.argon4w.acceleratedrendering.features.text.mixins;

import com.github.argon4w.acceleratedrendering.core.CoreFeature;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.VertexConsumerExtension;
import com.github.argon4w.acceleratedrendering.features.text.AcceleratedBakedGlyphRenderer;
import com.github.argon4w.acceleratedrendering.features.text.AcceleratedTextRenderingFeature;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ExtensionMethod(VertexConsumerExtension.class)
@Mixin(value = BakedGlyph.class, priority = Integer.MIN_VALUE)
public class BakedGlyphMixin {

    @Unique
    private final AcceleratedBakedGlyphRenderer normalRenderer = new AcceleratedBakedGlyphRenderer((BakedGlyph) (Object) this, false);
    @Unique
    private final AcceleratedBakedGlyphRenderer italicRenderer = new AcceleratedBakedGlyphRenderer((BakedGlyph) (Object) this, true);

    @Inject(method = "render(ZFFFLorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;IZI)V", at = @At("HEAD"), cancellable = true)
    public void renderFast(
            boolean pItalic,
            float pX,
            float pY,
            float pZ,
            Matrix4f pMatrix,
            VertexConsumer pBuffer,
            int pColor,
            boolean pBold,
            int pPackedLight,
            CallbackInfo ci
    ) {
        var extension = pBuffer.getAccelerated();

        if (CoreFeature.isRenderingLevel()
                && AcceleratedTextRenderingFeature.isEnabled()
                && AcceleratedTextRenderingFeature.shouldUseAcceleratedPipeline()
                && extension.isAccelerated()
        ) {
            ci.cancel();

            var renderer = pItalic
                    ? italicRenderer
                    : normalRenderer;

            extension.doRender(
                    renderer,
                    new Vector2f(pX, pY),
                    pMatrix,
                    null,
                    pPackedLight,
                    OverlayTexture.NO_OVERLAY,
                    ARGB.toABGR(pColor)
            );
        }
    }
}
