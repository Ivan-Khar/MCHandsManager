package one.theaq.handsmanager.data;

//? if fabric {

/*import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

@Entrypoint("fabric-datagen")
public final class HandsManagerDataGen implements DataGeneratorEntrypoint {
	
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
	
	}
}
*///?} else {

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import one.theaq.handsmanager.HandsManagerMain;

@EventBusSubscriber(modid = HandsManagerMain.MOD_ID)
public final class HandsManagerDataGen {

	//? if >= 1.21.4 {
	@SubscribeEvent
	public static void gatherServerData(GatherDataEvent.Server event) {

	}

	@SubscribeEvent
	public static void gatherClientData(GatherDataEvent.Client event) {

	}
	//?} else {
	
	/*@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
	
	}
	*///?}
}

//?}