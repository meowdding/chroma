package me.owdding.chroma.utils

import com.mojang.renderpearl.api.pipeline.RenderPipeline
import me.owdding.chroma.accessor.RenderPipelineBuilderAccessor
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.joml.component1
import org.joml.component2
import org.joml.component3
import org.joml.component4

internal fun RenderPipeline.Builder.withShaderDefine(name: String, array: IntArray): RenderPipeline.Builder {
    val accessor = this as RenderPipelineBuilderAccessor
    accessor.`meowddingchroma$define`(name, "int[](${array.joinToString(", ") { it.toString() }})")
    return this
}

internal fun RenderPipeline.Builder.withShaderDefine(name: String, array: FloatArray): RenderPipeline.Builder {
    val accessor = this as RenderPipelineBuilderAccessor
    accessor.`meowddingchroma$define`(name, "float[](${array.joinToString(", ") { it.toString() }})")
    return this
}

internal fun RenderPipeline.Builder.withShaderDefine(name: String, vector2f: Vector2f): RenderPipeline.Builder {
    val accessor = this as RenderPipelineBuilderAccessor
    accessor.`meowddingchroma$define`(name, "vec2(${vector2f.x}, ${vector2f.y})")
    return this
}

internal fun RenderPipeline.Builder.withShaderDefine(name: String, array: Array<Vector2f>): RenderPipeline.Builder {
    val accessor = this as RenderPipelineBuilderAccessor
    accessor.`meowddingchroma$define`(name, "vec2[](${array.joinToString(", ") { (x, y) -> "vec2($x, $y)" }})")
    return this
}

internal fun RenderPipeline.Builder.withShaderDefine(name: String, array: Array<Vector3f>): RenderPipeline.Builder {
    val accessor = this as RenderPipelineBuilderAccessor
    accessor.`meowddingchroma$define`(name, "vec3[](${array.joinToString(", ") { (x, y, z) -> "vec3($x, $y, $z)" }})")
    return this
}

internal fun RenderPipeline.Builder.withShaderDefine(name: String, array: Array<Vector4f>): RenderPipeline.Builder {
    val accessor = this as RenderPipelineBuilderAccessor
    accessor.`meowddingchroma$define`(name, "vec4[](${array.joinToString(", ") { (x, y, z, w) -> "vec4($x, $y, $z, $w)" }})")
    return this
}
