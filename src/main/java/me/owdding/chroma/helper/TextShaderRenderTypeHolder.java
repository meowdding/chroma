package me.owdding.chroma.helper;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.rendertype.RenderType;

public interface TextShaderRenderTypeHolder {

    RenderType meowddingchroma$getRenderType(RenderPipeline pipeline, Font.DisplayMode displayMode, Boolean grayScale);
}