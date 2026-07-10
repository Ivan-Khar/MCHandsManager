plugins {
    kotlin("jvm") version "2.4.0" apply false
    id("dev.kikugie.stonecutter")
    id("fabric-loom") version "1.17-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT" apply false
    id("net.neoforged.moddev") version "2.0.141" apply false
}

stonecutter active "26.2-fabric"

stonecutter parameters {
    var loader = node.metadata.project.substringAfterLast('-')
    constants.match(loader, "fabric", "neoforge")

    listOf("jei", "rei").forEach {
        constants[it] = node.project.hasProperty("deps.$it")
    }

    replacements.string(current.parsed < "26.0", "identifier") {
        replace("Identifier", "ResourceLocation")
        replace("identifier()", "location()")
    }

    replacements.string(current.parsed < "26.0", "itemInHandRenderer") {
        replace("submitArmWithItem", "renderArmWithItem")
        replace("submitHandsWithItems", "renderHandsWithItems")
        //TODO: This doesnt work particularly well,
        //  make sure to go between one loader versions and then switch the loaders if you need to
        if (loader == "fabric") {
            replace("name = \"isMainHand\"", "name = \"bl\"") // fabric 1.21
        } else {
            replace("name = \"isMainHand\"", "name = \"flag\"") // neof 1.21
        }

        replace(
            "Lnet/minecraft/client/renderer/ItemInHandRenderer;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z",
            "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
        )
        replace(
            "shouldInstantlyReplaceVisibleItem(mainHandItem, nMainHand)",
            "ItemStack.matches(mainHandItem, nMainHand)"
        )
        replace("\"offhandInverseArmHeight\"", "\"f\"")
        replace(
            "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)",
            "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)"
        )
        replace(
            "renderTwoHandedMap(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IFFF)",
            "renderTwoHandedMap(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IFFF)"
        )
        replace("SubmitNodeCollector submitter", "MultiBufferSource submitter")
        if (loader == "fabric") {
            replace("name = \"nextOffHand\"", "name = \"itemStack2\"")
            replace("name = \"nextMainHand\"", "name = \"itemStack\"")
        } else {
            replace("name = \"nextOffHand\"", "name = \"itemstack\"")
            replace("name = \"nextMainHand\"", "name = \"itemstack1\"")
        }
    }
}
