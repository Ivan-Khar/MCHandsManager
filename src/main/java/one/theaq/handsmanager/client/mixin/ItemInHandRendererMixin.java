package one.theaq.handsmanager.client.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import one.theaq.handsmanager.HandsManagerMain;
import one.theaq.handsmanager.config.HandsManagerConfig;
import one.theaq.handsmanager.tag.CommonTags;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment(value = "client")
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	@Shadow
	protected abstract void renderPlayerArm(PoseStack par1, SubmitNodeCollector par2, int par3, float par4, float par5, HumanoidArm par6);
	
	// submitArmWithItem runs twice, once for main hand and once for offhand.
	//  Make sure to check what InteractionHand is it
	
	@Shadow
	private ItemStack mainHandItem;
	@Shadow @Final
	private ItemModelResolver itemModelResolver;
	@Shadow
	private float oMainHandHeight;
	@Shadow
	private float mainHandHeight;
	@Unique
	private boolean wasTwoHandedItem = false;
	
	@Shadow
	protected abstract boolean shouldInstantlyReplaceVisibleItem(ItemStack par1, ItemStack par2);
	
	@Shadow
	@Final
	private Minecraft minecraft;
	
	@Shadow
	private static boolean isChargedCrossbow(ItemStack itemStack) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}
	
	// TODO:
	//  - Optionally make two handed map onehanded on main hand
	//  - Actual proper config where you can move hands with gizmo instead of sliders like I did before
	//  - Offhand just pops into the existence if you stop holding two handed item in your main hand
	@Unique
	HandsManagerConfig CONFIG = HandsManagerMain.CONFIG;
	@Unique
	Logger LOGGER = HandsManagerMain.LOGGER;
	
	@Definition(id = "isMainHand", local = @Local(type = boolean.class, name = "isMainHand"))
	@Expression("isMainHand")
	@ModifyExpressionValue(method = "submitArmWithItem", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
	private boolean addDoubleHandRenderer(boolean original) {
		return !mainHandItem.has(DataComponents.MAP_ID) || mainHandItem.is(CommonTags.CROSSBOWS);
	}
	
	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
	private boolean hideHandIfTwoHandedItem(boolean original, @Local(name = "nextMainHand") ItemStack nextMainHand, @Local(name = "nextOffHand") ItemStack nextOffHand) {
		return wasTwoHandedItem ? shouldInstantlyReplaceVisibleItem(mainHandItem, nextMainHand) : original;
	}
	
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
	private void checkIfHeldTwoHandedItem(CallbackInfo ci, @Local(name = "nextMainHand") ItemStack nextMainHand, @Local(name = "nextOffHand") ItemStack nextOffHand) {
		if (mainHandItem.has(DataComponents.MAP_ID) || nextMainHand.has(DataComponents.MAP_ID) || mainHandItem.is(CommonTags.CROSSBOWS)) wasTwoHandedItem = true;
	}
	
	@ModifyVariable(method = "submitHandsWithItems", name = "offhandInverseArmHeight", at = @At("LOAD"))
	private float modifyOffhandInverseArmHeight(float offhandInverseArmHeight, @Local(argsOnly = true) float frameInterp) {
		if (wasTwoHandedItem) {
			var mainHandInverseArmHeight = itemModelResolver.swapAnimationScale(mainHandItem) * (1.0F - Mth.lerp(frameInterp, oMainHandHeight, mainHandHeight));
			
			if (mainHandInverseArmHeight == 0) wasTwoHandedItem = false;
			return mainHandInverseArmHeight;
		}
		return offhandInverseArmHeight;
	}
	
	@Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderTwoHandedMap(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IFFF)V", ordinal = 0))
	private void removeXYOffsetForTwoHandedMap(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
		if (hand.equals(InteractionHand.OFF_HAND)) poseStack.translate(-CONFIG.leftXOffset, -CONFIG.leftYOffset, 0);
	}
	
	@Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", ordinal = 0))
	private void removeXYOffsetForTwoHandedCrossbow(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
		if (hand.equals(InteractionHand.OFF_HAND)) poseStack.translate(-CONFIG.leftXOffset, -CONFIG.leftYOffset, 0);
	}
	
}
