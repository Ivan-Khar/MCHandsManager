package one.theaq.handsmanager.client.mixin;

//~itemInHandRenderer
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
//? >= 26.0 {
/*import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModelResolver;
*///? } else {
import net.minecraft.client.renderer.MultiBufferSource;
 //? }
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
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

@SuppressWarnings("NameDoesntMatchTargetClass")
@MixinEnvironment(value = "client")
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	//? if < 26.0 {
	@Shadow
	protected abstract void renderPlayerArm(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f, float g, HumanoidArm humanoidArm);
	//? } else {
	/*@Shadow
	protected abstract void renderPlayerArm(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float inverseArmHeight, float attackValue, HumanoidArm arm);
	
	@Shadow
	@Final
	private ItemModelResolver itemModelResolver;
	
	@Shadow
	protected abstract boolean shouldInstantlyReplaceVisibleItem(ItemStack currentlyVisibleItem, ItemStack expectedItem);
	
	
	*///? }
	@Shadow
	private ItemStack mainHandItem;
	@Shadow
	private float oMainHandHeight;
	@Shadow
	private float mainHandHeight;
	@Unique
	private boolean wasTwoHandedItem = false;
	
	@Shadow
	@Final
	private Minecraft minecraft;
	
	@Shadow
	private static boolean isChargedCrossbow(ItemStack item) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}
	
	@Shadow
	private ItemStack offHandItem;
	
	// TODO:
	//  - Optionally make two handed map onehanded on main hand
	//  - Actual proper config where you can move hands with gizmo instead of sliders like I did before
	@Unique
	HandsManagerConfig handsManager$CONFIG = HandsManagerMain.INSTANCE.getCONFIG();
	@Unique
	Logger handsManager$LOGGER = HandsManagerMain.INSTANCE.getLOGGER();
	
	@Definition(id = "isMainHand", local = @Local(type = boolean.class, name = "bl"))
	@Expression("isMainHand")
	@ModifyExpressionValue(method = "renderArmWithItem", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
	private boolean addDoubleHandRenderer(boolean original) {
		return !mainHandItem.has(DataComponents.MAP_ID) || mainHandItem.is(CommonTags.INSTANCE.getCROSSBOWS());
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
	private boolean hideHandIfTwoHandedItem(boolean original, @Local(name = "itemStack") ItemStack nMainHand, @Local(name = "itemStack2") ItemStack nOffHand) {
		return wasTwoHandedItem ? ItemStack.matches(mainHandItem, nMainHand) : original;
	}
	
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
	private void checkIfHeldTwoHandedItem(CallbackInfo ci, @Local(name = "itemStack") ItemStack nMainHand, @Local(name = "itemStack2") ItemStack nOffHand) {
		if ((mainHandItem.has(DataComponents.MAP_ID) ||
				     nMainHand.has(DataComponents.MAP_ID) ||
				     (mainHandItem.is(CommonTags.INSTANCE.getCROSSBOWS()) && CrossbowItem.isCharged(mainHandItem))
		) && offHandItem.isEmpty()) wasTwoHandedItem = true;
	}
	
	@ModifyVariable(method = "renderHandsWithItems", name = "f", at = @At("LOAD"))
	private float modifyOffhandInverseArmHeight(float offhandInverseArmHeight, @Local(argsOnly = true, type = Float.class, ordinal = 0) float fInterp) {
		if (wasTwoHandedItem) {
			//? if < 26.0 {
			var mainHandInverseArmHeight = 1.0F - Mth.lerp(fInterp, this.oMainHandHeight, this.mainHandHeight);
			//? } else {
			/*var mainHandInverseArmHeight = itemModelResolver.swapAnimationScale(mainHandItem) * (1.0F - Mth.lerp(fInterp, oMainHandHeight, mainHandHeight));
			*///? }
			
			if (mainHandInverseArmHeight == 0) wasTwoHandedItem = false;
			return mainHandInverseArmHeight;
		}
		return offhandInverseArmHeight;
	}
	
	@Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderTwoHandedMap(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IFFF)V", ordinal = 0))
	private void removeXYOffsetForTwoHandedMap(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, MultiBufferSource submitter, int lightCoords, CallbackInfo ci) {
		if (hand.equals(InteractionHand.OFF_HAND))
			poseStack.translate(-handsManager$CONFIG.getLeftXOffset(), -handsManager$CONFIG.getLeftYOffset(), 0);
	}
	
	@Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", ordinal = 0))
	private void removeXYOffsetForTwoHandedCrossbow(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, MultiBufferSource submitter, int lightCoords, CallbackInfo ci) {
		if (hand.equals(InteractionHand.OFF_HAND))
			poseStack.translate(-handsManager$CONFIG.getLeftXOffset(), -handsManager$CONFIG.getLeftYOffset(), 0);
	}
}
