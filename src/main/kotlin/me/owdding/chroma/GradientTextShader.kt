package me.owdding.chroma

import com.mojang.renderpearl.api.pipeline.DepthStencilState
import com.mojang.renderpearl.api.pipeline.RenderPipeline
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.owdding.chroma.MeowddingChroma.id
import me.owdding.chroma.generated.EnumCodec
import me.owdding.chroma.utils.MemoizeUtil
import me.owdding.chroma.utils.withShaderDefine
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.TextColor
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB
import org.joml.Vector4f
import com.mojang.renderpearl.api.pipeline.ColorTargetState
import com.mojang.renderpearl.api.pipeline.CompareOp

//? if < 26.3 {
/*import com.mojang.renderpearl.api.pipeline.BindGroupLayout
import me.owdding.chroma.utils.withShaderDefine
*///?}
//? if >= 26.3
import net.minecraft.client.renderer.oit.OitPipelineSet

private fun RenderPipeline.Builder.applyShaderParams(shader: GradientTextShader, displayMode: Font.DisplayMode?, grayscale: Boolean): RenderPipeline.Builder {
    return this.withLocation(id("gradient_text/${displayMode?.name?.lowercase()}${if (grayscale) "_grayscale" else ""}"))
        .withVertexShader(id("text/gradient"))
        .withFragmentShader(id("text/gradient"))
        //? if < 26.3
        //.withBindGroupLayout(BindGroupLayout.builder().build())
        .apply {
            if (grayscale) {
                withShaderDefine("IS_GRAYSCALE")
            }

            when (displayMode) {
                Font.DisplayMode.SEE_THROUGH -> {
                    withShaderDefine("IS_SEE_THROUGH")
                }

                Font.DisplayMode.POLYGON_OFFSET -> {
                    withDepthStencilState(DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true, 1.0F, 10.0F))
                }

                null -> {
                    withShaderDefine("IS_GUI")
                }

                else -> {}
            }
        }
        .withShaderDefine(
            "COLORS",
            shader.gradient
                .getColors()
                .map { color -> Vector4f(ARGB.redFloat(color), ARGB.greenFloat(color), ARGB.blueFloat(color), ARGB.alphaFloat(color)) }
                .toTypedArray(),
        )
        .withShaderDefine(
            "DIRECTION",
            shader.gradientDirection.vec,
        )
        .withShaderDefine(
            "SPEED",
            shader.speed,
        )
}

//? if >= 26.3 {
private val pipelineCreator0: (GradientTextShader, Font.DisplayMode?, Boolean) -> Pair<RenderPipeline, OitPipelineSet?> =
    MemoizeUtil.memoize { shader, displayMode, grayscale ->
        val location = id("gradient_text/${displayMode?.name?.lowercase()}${if (grayscale) "_grayscale" else ""}")
        val builder = RenderPipeline.builder(
            *buildList {
                when (displayMode) {
                    NORMAL, POLYGON_OFFSET -> add(RenderPipelines.WORLD_TEXT_SNIPPET)
                    SEE_THROUGH -> add(RenderPipelines.TEXT_SNIPPET)
                    null -> add(RenderPipelines.GUI_TEXT_SNIPPET)
                }
            }.toTypedArray(),
        ).applyShaderParams(shader, displayMode, grayscale)

        if (displayMode == null) {
            return@memoize RenderPipelines.register(builder.withColorTargetState(ColorTargetState(TRANSLUCENT)).build()) to null
        }

        val snippet = builder.buildSnippet()

        RenderPipelines.register(
            RenderPipeline.builder(snippet).withColorTargetState(ColorTargetState(TRANSLUCENT)).withLocation(location).build(),
        ) to RenderPipelines.register(OitPipelineSet.builder("meowdding/gradient_text_shader", builder).build())
    }
//? } elif 26.2 {
/*private val pipelineCreator0: (GradientTextShader, Font.DisplayMode?, Boolean) -> RenderPipeline = MemoizeUtil.memoize { shader, displayMode, grayscale ->
    RenderPipelines.register(
        RenderPipeline.builder(
            *buildList {
                when (displayMode) {
                    Font.DisplayMode.NORMAL, Font.DisplayMode.POLYGON_OFFSET -> add(RenderPipelines.WORLD_TEXT_SNIPPET)
                    Font.DisplayMode.SEE_THROUGH -> add(RenderPipelines.TEXT_SNIPPET)
                    null -> add(RenderPipelines.GUI_TEXT_SNIPPET)
                }
            }.toTypedArray(),
        ).applyStuff(shader, displayMode, grayscale).build(),
    )
}
*///? }

class GradientTextShader(val gradient: GradientProvider, val gradientDirection: GradientDirection = RIGHT, val speed: Float = 1f) : TextShader {
    constructor(colors: List<Int>) : this({ colors })
    constructor(colors: List<Int>, direction: GradientDirection = RIGHT, speed: Float = 1f) : this({ colors }, direction, speed)
    constructor(vararg colors: TextColor) : this(colors.map { it.value })
    constructor(vararg colors: TextColor, direction: GradientDirection = RIGHT, speed: Float = 1f) : this(colors.map { it.value }, direction, speed)

    override val id: Identifier = ID

    override val useWhite: Boolean get() = true
    override val hasShadow: Boolean get() = true


    //? if <= 26.1
    //override val pipeline: RenderPipeline = pipelineCreator0(this)

    override val pipeline: (Font.DisplayMode?, Boolean) -> PipelineResult = { first, second -> pipelineCreator0(this, first, second) }

    companion object {
        val ID = id("gradient")
        val CODEC: MapCodec<GradientTextShader> = RecordCodecBuilder.mapCodec {
            it.group(
                GradientProvider.CODEC.fieldOf("colors").forGetter { it.gradient },
                EnumCodec.forKCodec(GradientDirection.entries.toTypedArray()).optionalFieldOf("direction", GradientDirection.RIGHT)
                    .forGetter { it.gradientDirection },
                Codec.FLOAT.optionalFieldOf("speed", 1f).forGetter { it.speed },
            ).apply(it, ::GradientTextShader)
        }

        val pipelineCreator: (GradientTextShader, Font.DisplayMode?, Boolean) -> PipelineResult = pipelineCreator0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GradientTextShader) return false

        if (speed != other.speed) return false
        if (gradient.getColors() != other.gradient.getColors()) return false
        if (gradientDirection != other.gradientDirection) return false

        return true
    }

    override fun hashCode(): Int {
        var result = speed.hashCode()
        result = 31 * result + gradient.getColors().hashCode()
        result = 31 * result + gradientDirection.hashCode()
        return result
    }
}
