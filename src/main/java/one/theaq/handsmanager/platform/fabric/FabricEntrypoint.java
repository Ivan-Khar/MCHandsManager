//? if fabric {
package one.theaq.handsmanager.platform.fabric;

import one.theaq.handsmanager.HandsManagerMain;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;

@Entrypoint
public final class FabricEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        HandsManagerMain.initialize();
    }
}
//?}
