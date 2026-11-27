package me.owdding.chroma.helper;

import me.owdding.chroma.TextShader;
import net.minecraft.network.chat.Style;
import org.apache.commons.lang3.NotImplementedException;

public interface TextShaderHolder {

    default TextShader meowddingchroma$getTextShader() {
        throw new NotImplementedException("Implemented via mixins!");
    }

    default Style meowddingchroma$withTextShader(TextShader shader) {
        throw new NotImplementedException("Implemented via mixins!");
    }
}