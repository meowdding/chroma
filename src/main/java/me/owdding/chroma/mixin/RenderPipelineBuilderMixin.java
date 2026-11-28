package me.owdding.chroma.mixin;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import java.util.Optional;
import me.owdding.chroma.accessor.RenderPipelineBuilderAccessor;
import net.minecraft.client.renderer.ShaderDefines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderPipeline.Builder.class)
public class RenderPipelineBuilderMixin implements RenderPipelineBuilderAccessor {

    @Shadow
    private Optional<ShaderDefines.Builder> definesBuilder;

    @Override
    public void meowddingchroma$define(String name, String value) {
        if (this.definesBuilder.isEmpty()) {
            this.definesBuilder = Optional.of(ShaderDefines.builder());
        }
        this.definesBuilder.get().define(name, value);
    }
}
