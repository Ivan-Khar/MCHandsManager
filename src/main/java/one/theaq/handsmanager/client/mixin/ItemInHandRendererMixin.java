package one.theaq.handsmanager.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
	
	@Unique
	HandsManagerConfig CONFIG = HandsManagerMain.CONFIG;
	
	@Inject(
			method = "submitArmWithItem",
			at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", ordinal = 0),
			cancellable = true
	)
	private void armWithItemHead(
			AbstractClientPlayer player,
			float frameInterp,
			float xRot,
			InteractionHand hand,
			float attack,
			ItemStack itemStack,
			float inverseArmHeight,
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			int lightCoords,
			CallbackInfo ci
	) {
		if (hand.equals(InteractionHand.OFF_HAND)) {
			
			poseStack.pushPose();
			poseStack.mulPose(Axis.ZP.rotationDegrees(-1 * 10.0F));
			poseStack.translate(CONFIG.leftXOffset + 0.25, CONFIG.leftYOffset - 0.5, CONFIG.leftZOffset + 0.25);
			this.renderPlayerArm(poseStack, submitNodeCollector, lightCoords, inverseArmHeight, attack, HumanoidArm.LEFT);
			poseStack.popPose();
			
			poseStack.translate(CONFIG.leftXOffset, CONFIG.leftYOffset, CONFIG.leftZOffset);
		}
	}
	
	@Inject(
			method = "submitArmWithItem",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderTwoHandedMap(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IFFF)V", ordinal = 0),
			cancellable = true
	)
	private void removeOffsetForTwoHandedMap(
			AbstractClientPlayer player,
			float frameInterp,
			float xRot,
			InteractionHand hand,
			float attack,
			ItemStack itemStack,
			float inverseArmHeight,
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			int lightCoords,
			CallbackInfo ci
	) {
		if (hand.equals(InteractionHand.OFF_HAND)) poseStack.translate(-CONFIG.leftXOffset, -CONFIG.leftYOffset, 0);
	}
	
	@Inject(
			method = "submitArmWithItem",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", ordinal = 0),
			cancellable = true
	)
	private void anyItemRendererHead(
			AbstractClientPlayer player,
			float frameInterp,
			float xRot,
			InteractionHand hand,
			float attack,
			ItemStack itemStack,
			float inverseArmHeight,
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			int lightCoords,
			CallbackInfo ci
	) {
		if (hand.equals(InteractionHand.OFF_HAND)) poseStack.translate(-CONFIG.leftXOffset, -CONFIG.leftYOffset, 0);
	}
	
}
