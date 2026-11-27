package me.owdding.chroma

import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.minecraft.resources.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object MeowddingChroma : ModInitializer {
    const val ID: String = "meowdding_chroma"
    val CONTAINER: ModContainer = FabricLoader.getInstance().allMods.find { it.metadata.id == ID }!!
    val VERSION: String = CONTAINER.metadata.version.toString()

    val LOGGER: Logger = LoggerFactory.getLogger(ID)

    fun id(string: String): Identifier = Identifier.fromNamespaceAndPath(ID, string)

    override fun onInitialize() {
        LOGGER.info("Hello Meowdding Chroma!")
    }
}
