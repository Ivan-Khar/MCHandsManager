plugins {
    kotlin("jvm") version "2.4.0" apply false
    id("dev.kikugie.stonecutter")
    id("fabric-loom") version "1.17-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT" apply false
    id("net.neoforged.moddev") version "2.0.141" apply false
}

stonecutter active "1.21.1-fabric"

stonecutter parameters {
    constants.match(node.metadata.project.substringAfterLast('-'), "fabric", "neoforge")

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
        replace("name = \"isMainHand\"", "name = \"bl\"")
        replace(
            "Lnet/minecraft/client/renderer/ItemInHandRenderer;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z",
            "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
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
        replace("nextOffHand", "itemStack2")
        replace("nextMainHand", "itemStack")
    }
}
