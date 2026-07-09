package one.theaq.handsmanager

//~identifier
import net.minecraft.resources.Identifier
import one.theaq.handsmanager.config.HandsManagerConfig
import one.theaq.handsmanager.tag.CommonTags
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object HandsManagerMain {
    const val MOD_ID: String = "mchandsmanager"
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
    val CONFIG: HandsManagerConfig = HandsManagerConfig()

    fun initialize() {
        CommonTags.init()
        LOGGER.info("$MOD_ID initialized")
    }

    fun location(path: String): Identifier {
        return Identifier.fromNamespaceAndPath(MOD_ID, path)
    }

    fun commonLocation(path: String): Identifier {
        return Identifier.fromNamespaceAndPath("c", path)
    }
}