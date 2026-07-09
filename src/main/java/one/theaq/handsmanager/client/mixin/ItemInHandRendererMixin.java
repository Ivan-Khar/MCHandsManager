package one.theaq.handsmanager.client.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import one.theaq.handsmanager.HandsManagerMain;
import one.theaq.handsmanager.config.HandsManagerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment(value = "client")
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	@Shadow
	protected abstract void renderPlayerArm(PoseStack par1, SubmitNodeCollector par2, int par3, float par4, float par5, HumanoidArm par6);
	
	// submitArmWithItem runs twice, once for main hand and once for offhand.
	//  Make sure to check what InteractionHand is it
	
	// TODO:
	//  - Optionally make two handed map onehanded on main hand
	//  - Disable offhand rendering if two handed item is in main hand
	//  - Actual proper config where you can move hands with gizmo instead of sliders like I did before
	
	@Unique
	HandsManagerConfig CONFIG = HandsManagerMain.CONFIG;
	
	@Definition(id = "isMainHand", local = @Local(type = boolean.class, name = "isMainHand"))
	@Expression("isMainHand")
	@ModifyExpressionValue(method = "submitArmWithItem", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
	private boolean test(boolean original) {
		return true;
	}
	
	@Inject(
			method = "submitArmWithItem",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderTwoHandedMap(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IFFF)V", ordinal = 0),
			cancellable = true
	)
	private void removeXYOffsetForTwoHandedMap(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
		if (hand.equals(InteractionHand.OFF_HAND)) poseStack.translate(-CONFIG.leftXOffset, -CONFIG.leftYOffset, 0);
	}
	
	@Inject(
			method = "submitArmWithItem",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", ordinal = 0),
			cancellable = true
	)
	private void removeXYOffsetForTwoHandedCrossbow(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
		if (hand.equals(InteractionHand.OFF_HAND)) poseStack.translate(-CONFIG.leftXOffset, -CONFIG.leftYOffset, 0);
	}
	
}
