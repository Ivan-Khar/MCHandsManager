package one.theaq.handsmanager.data

//? if fabric {
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import one.theaq.handsmanager.data.fabric.DataGenTagProvider

@Entrypoint("fabric-datagen")
class HandsManagerDataGen: DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack = fabricDataGenerator.createPack()
        pack.addProvider(::DataGenTagProvider)
    }
}
//?} else {

/*import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import one.theaq.handsmanager.HandsManagerMain

@EventBusSubscriber(modid = HandsManagerMain.MOD_ID)
object HandsManagerDataGen {
    //? if >= 1.21.4 {
    @SubscribeEvent
    fun gatherServerData(event: GatherDataEvent.Server) {

    }

    @SubscribeEvent
    fun gatherClientData(event: GatherDataEvent.Client) {

    }
    //?} else {
    /*@SubscribeEvent
    fun gatherData(event: GatherDataEvent) {

    }
    *///?}
}

*///?}