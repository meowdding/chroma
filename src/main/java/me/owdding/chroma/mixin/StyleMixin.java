package me.owdding.chroma.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.owdding.chroma.TextShader;
import me.owdding.chroma.helper.TextShaderHolder;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Objects;

@Mixin(Style.class)
public abstract class StyleMixin implements TextShaderHolder {

    @Unique
    private final static ThreadLocal<TextShader> meowddingchroma$SHADER = new ThreadLocal<>();
    @Shadow
    @Final
    @Nullable
    FontDescription font;
    @Shadow
    @Final
    @Nullable
    String insertion;
    @Shadow
    @Final
    @Nullable
    HoverEvent hoverEvent;
    @Shadow
    @Final
    @Nullable
    ClickEvent clickEvent;
    @Shadow
    @Final
    @Nullable
    Boolean obfuscated;
    @Shadow
    @Final
    @Nullable
    Boolean strikethrough;
    @Shadow
    @Final
    @Nullable
    Boolean underlined;
    @Shadow
    @Final
    @Nullable
    Boolean italic;
    @Shadow
    @Final
    @Nullable
    Boolean bold;
    @Shadow
    @Final
    @Nullable
    Integer shadowColor;
    @Shadow
    @Final
    @Nullable
    TextColor color;
    @Unique
    @Nullable
    private TextShader meowddingchroma$textShader;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if (meowddingchroma$SHADER == null) {
            return;
        }
        this.meowddingchroma$textShader = meowddingchroma$SHADER.get();
    }

    @WrapOperation(method = {
            "withColor(Lnet/minecraft/network/chat/TextColor;)Lnet/minecraft/network/chat/Style;",
            "withShadowColor",
            "withBold",
            "withItalic",
            "withUnderlined",
            "withStrikethrough",
            "withObfuscated",
            "withClickEvent",
            "withHoverEvent",
            "withInsertion",
            "withFont",
            "applyFormat",
            "applyFormats",
            "applyLegacyFormat",
            "applyTo"
    }, at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/network/chat/TextColor;Ljava/lang/Integer;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Lnet/minecraft/network/chat/ClickEvent;Lnet/minecraft/network/chat/HoverEvent;Ljava/lang/String;Lnet/minecraft/network/chat/FontDescription;)Lnet/minecraft/network/chat/Style;"
    ))
    public Style copy(
            TextColor textColor,
            Integer shadowColor,
            Boolean bold,
            Boolean italic,
            Boolean underlined,
            Boolean strikethrough,
            Boolean obfuscated,
            ClickEvent clickEvent,
            HoverEvent hoverEvent,
            String insertion,
            FontDescription fontDescription,
            Operation<Style> original
    ) {
        var previous = meowddingchroma$SHADER.get();
        meowddingchroma$SHADER.set(meowddingchroma$textShader);
        var style = original.call(
                textColor,
                shadowColor,
                bold,
                italic,
                underlined,
                strikethrough,
                obfuscated,
                clickEvent,
                hoverEvent,
                insertion,
                fontDescription
        );
        meowddingchroma$SHADER.set(previous);
        return style;
    }

    @Override
    public TextShader meowddingchroma$getTextShader() {
        return this.meowddingchroma$textShader;
    }

    @Override
    public Style meowddingchroma$withTextShader(TextShader shader) {
        var previous = meowddingchroma$SHADER.get();
        meowddingchroma$SHADER.set(shader);
        var value = Objects.equals(this.meowddingchroma$textShader, shader)
                ? ((Style) (Object) this)
                : Style.checkEmptyAfterChange(
                new Style(
                        this.color,
                        this.shadowColor,
                        this.bold,
                        this.italic,
                        this.underlined,
                        this.strikethrough,
                        this.obfuscated,
                        this.clickEvent,
                        this.hoverEvent,
                        this.insertion,
                        this.font
                ),
                this.meowddingchroma$textShader,
                meowddingchroma$textShader);
        meowddingchroma$SHADER.set(previous);
        return value;
    }

    @ModifyReturnValue(method = "equals", at = @At(value = "RETURN"))
    public boolean equals(boolean original, @Local(argsOnly = true) Object other) {
        if (other instanceof TextShaderHolder otherHolder) {
            return original && otherHolder.meowddingchroma$getTextShader() == this.meowddingchroma$textShader;
        }
        return original;
    }
}