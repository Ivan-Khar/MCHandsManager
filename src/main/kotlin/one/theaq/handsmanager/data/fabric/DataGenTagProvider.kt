package one.theaq.handsmanager.data.fabric

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Items
import one.theaq.handsmanager.tag.CommonTags
import java.util.concurrent.CompletableFuture

class DataGenTagProvider(output: FabricPackOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>): FabricTagsProvider.ItemTagsProvider(output, registriesFuture) {
    override fun addTags(registries: HolderLookup.Provider) {
        builder(CommonTags.CROSSBOWS)
            .add(Items.FEATHER.builtInRegistryHolder().key())
            .setReplace(false)
    }

}