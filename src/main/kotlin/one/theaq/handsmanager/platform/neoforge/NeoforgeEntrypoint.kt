//? if neoforge {
package one.theaq.handsmanager.platform.neoforge

import net.neoforged.fml.common.Mod
import one.theaq.handsmanager.HandsManagerMain

@Mod(HandsManagerMain.MOD_ID)
class NeoforgeEntrypoint {
    init {
        HandsManagerMain.initialize()
    }
}
//?}