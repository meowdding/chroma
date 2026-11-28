package me.owdding.chroma.test

import me.owdding.chroma.MeowddingChroma.id
import me.owdding.chroma.PrideShader
import me.owdding.chroma.textShader
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

object MeowddingChromaTest : ClientModInitializer {
    override fun onInitializeClient() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.SLEEP, id("chroma_test")) { graphics, _ ->
            val font = Minecraft.getInstance().font

            mapOf(
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr," to PrideShader.RAINBOW,
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore" to PrideShader.BISEXUAL.withDirectionAndSpeed(DOWN, 4f),
                "magna aliquyam erat, sed diam voluptua." to PrideShader.ASEXUAL.withDirectionAndSpeed(DOWN_RIGHT, 10f),
            ).entries.forEachIndexed { index, entry ->
                graphics.text(
                    font,
                    Component.literal(entry.key).apply { textShader = entry.value },
                    10,
                    10 + font.lineHeight * index,
                    -1,
                )
            }
        }
    }
}
