package me.owdding.chroma.accessor;

import me.owdding.chroma.TextShader;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.jetbrains.annotations.Nullable;

public interface FontPipelineHolder {

    ThreadLocal<TextShader> ACTIVE_PIPELINE = new ThreadLocal<>();

    static @Nullable FontPipelineHolder getHolder(Object instance) {
        return instance instanceof FontPipelineHolder holder ? holder : null;
    }

    @Nullable TextShader meowddingchroma$getPipeline();

    void meowddingchroma$setPipeline(TextShader pipeline);

    interface FontPipelineCreator extends FontPipelineHolder {
        static @Nullable FontPipelineCreator getHolder(Object instance) {
            return instance instanceof FontPipelineCreator holder ? holder : null;
        }

        RenderType meowddingchroma$createType(TextShader shader, Font.DisplayMode displayMode);

    }
}
