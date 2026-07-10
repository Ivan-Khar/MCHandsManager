//? if fabric {
package one.theaq.handsmanager.client.platform.fabric

import one.theaq.handsmanager.client.HandsManagerClient
import net.fabricmc.api.ClientModInitializer

object FabricClientEntrypoint: ClientModInitializer {
    override fun onInitializeClient() {
        HandsManagerClient.initialize()
    }
}
//?}