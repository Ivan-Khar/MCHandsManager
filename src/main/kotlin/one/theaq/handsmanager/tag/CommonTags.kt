package one.theaq.handsmanager.tag

import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import one.theaq.handsmanager.HandsManagerMain.commonLocation

object CommonTags {
    var CROSSBOWS = createCommonTag(Registries.ITEM, "tools/crossbow")

    fun <registryType : Any> createCommonTag(
        registry: ResourceKey<Registry<registryType>>,
        location: String
    ): TagKey<registryType> {
        return TagKey.create(registry, commonLocation(location))
    }
}