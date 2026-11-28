package me.owdding.chroma.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.owdding.chroma.TextShader;
import me.owdding.chroma.TextShaderKt;
import me.owdding.chroma.TextShaders;
import me.owdding.chroma.accessor.FontPipelineHolder;
import me.owdding.chroma.helper.TextShaderRenderTypeHolder;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.GlyphRenderTypes;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GlyphRenderTypes.class)
public abstract class GlyphRenderTypesMixin implements TextShaderRenderTypeHolder, FontPipelineHolder.FontPipelineCreator {

    @Unique
    private Identifier meowddingchroma$texture;
    @Unique
    private Boolean meowddingchroma$grayscale = false;

    @ModifyReturnValue(method = {"createForColorTexture", "createForIntensityTexture", "createForGrayscaleTexture"}, at = @At("RETURN"))
    private static GlyphRenderTypes create(GlyphRenderTypes original, @Local(argsOnly = true) Identifier name) {
        ((GlyphRenderTypesMixin) ((Object) original)).meowddingchroma$texture = name;
        return original;
    }

    @ModifyReturnValue(method = {"createForGrayscaleTexture"}, at = @At("RETURN"), require = 0)
    private static GlyphRenderTypes grayscale(GlyphRenderTypes original) {
        ((GlyphRenderTypesMixin) ((Object) original)).meowddingchroma$grayscale = true;
        return original;
    }

    @Inject(method = "select", at = @At("HEAD"), cancellable = true)
    private void select(Font.DisplayMode mode, CallbackInfoReturnable<RenderType> cir) {
        var shader = TextShaders.getActiveShader();
        if (shader != null) {
            cir.setReturnValue(meowddingchroma$createType(shader, mode));
        }
    }

    @Override
    public RenderType meowddingchroma$createType(TextShader shader, Font.DisplayMode displayMode) {
        return TextShaderKt.createTextRenderType(shader, meowddingchroma$texture, displayMode, meowddingchroma$grayscale);
    }
}
