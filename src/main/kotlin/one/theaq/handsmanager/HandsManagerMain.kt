package one.theaq.handsmanager

//~identifier
import net.minecraft.resources.ResourceLocation
import one.theaq.handsmanager.config.HandsManagerConfig
import one.theaq.handsmanager.tag.CommonTags
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object HandsManagerMain {
    const val MOD_ID: String = "mchandsmanager"
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
    val CONFIG: HandsManagerConfig = HandsManagerConfig()

    fun initialize() {
        CommonTags
        LOGGER.info("$MOD_ID initialized")
    }

    fun location(path: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
    }

    fun commonLocation(path: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath("c", path)
    }
}