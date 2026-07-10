//? if fabric {
package one.theaq.handsmanager.data.fabric

//? if >= 26.0 {
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
//? } else {
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
*///? }

import net.minecraft.core.HolderLookup
import java.util.concurrent.CompletableFuture
//? if >= 26.0 {

class DataGenTagProvider(output: FabricPackOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>
): FabricTagsProvider.ItemTagsProvider(output, registriesFuture) {
    override fun addTags(registries: HolderLookup.Provider) {

    }
}

//? } else {
/*class DataGenTagProvider(output: FabricDataOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>
): FabricTagProvider.ItemTagProvider(output, registriesFuture) {
    override fun addTags(provider: HolderLookup.Provider) {

    }
}
*///? }
//? }