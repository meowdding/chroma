package me.owdding.chroma.mixin;

import me.owdding.chroma.TextShader;
import me.owdding.chroma.accessor.FontPipelineHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(targets = {
    "net.minecraft.client.renderer.state.gui.GuiTextRenderState",
    "net.minecraft.client.gui.Font$PreparedTextBuilder",
    "net.minecraft.client.gui.font.glyphs.BakedSheetGlyph$GlyphInstance",
    "net.minecraft.client.gui.font.glyphs.BakedSheetGlyph$EffectInstance",
})
public class FontPipelineHolderMixin implements FontPipelineHolder {

    @Unique
    private TextShader meowddingchroma$pipeline;

    @Override
    public TextShader meowddingchroma$getPipeline() {
        return this.meowddingchroma$pipeline;
    }

    @Override
    public void meowddingchroma$setPipeline(TextShader pipeline) {
        this.meowddingchroma$pipeline = pipeline;
    }
}
