package foundationgames.enhancedblockentities.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import foundationgames.enhancedblockentities.EnhancedBlockEntityRegistry;
import foundationgames.enhancedblockentities.client.model.ShulkerLidOnlyModel;
import foundationgames.enhancedblockentities.util.duck.AppearanceStateHolder;
import foundationgames.enhancedblockentities.util.duck.EBEShulkerBoxRenderState;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.client.renderer.blockentity.state.ShulkerBoxRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxRenderer.class)
public class ShulkerBoxRendererMixin {
    @Unique
    private static final ThreadLocal<Boolean> enhanced_bes$renderOnlyLid = ThreadLocal.withInitial(() -> false);

    @Inject(method = "extractRenderState(Lnet/minecraft/world/level/block/entity/ShulkerBoxBlockEntity;Lnet/minecraft/client/renderer/blockentity/state/ShulkerBoxRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V", at = @At("TAIL"))
    private void enhanced_bes$markStaticModelLidOnly(ShulkerBoxBlockEntity blockEntity, ShulkerBoxRenderState state,
            float tickDelta, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, CallbackInfo ci) {
        boolean renderOnlyLid = blockEntity instanceof AppearanceStateHolder stateHolder
                && stateHolder.getRenderState() > 0
                && EnhancedBlockEntityRegistry.STATIC_MODEL_BLOCKS.contains(blockEntity.getBlockState().getBlock());
        ((EBEShulkerBoxRenderState) state).enhanced_bes$renderOnlyLid(renderOnlyLid);
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/blockentity/state/ShulkerBoxRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("HEAD"))
    private void enhanced_bes$beginLidOnlySubmit(ShulkerBoxRenderState state, PoseStack matrices,
            SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        enhanced_bes$renderOnlyLid.set(((EBEShulkerBoxRenderState) state).enhanced_bes$renderOnlyLid());
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/blockentity/state/ShulkerBoxRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("RETURN"))
    private void enhanced_bes$endLidOnlySubmit(ShulkerBoxRenderState state, PoseStack matrices,
            SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        enhanced_bes$renderOnlyLid.remove();
    }

    @SuppressWarnings("null")
@Redirect(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IIFLnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;Lnet/minecraft/client/resources/model/sprite/SpriteId;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;IIILnet/minecraft/client/resources/model/sprite/SpriteId;Lnet/minecraft/client/resources/model/sprite/SpriteGetter;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"
            )
    )
    private <S> void enhanced_bes$submitLidOnlyForStaticShulker(SubmitNodeCollector collector, Model<S> model,
            S animationState, PoseStack matrices, int light, int overlay, int color, SpriteId spriteId,
            SpriteGetter spriteGetter, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        if (enhanced_bes$renderOnlyLid.get() && animationState instanceof Float openAmount) {
            @SuppressWarnings("unchecked")
            Model<Float> shulkerModel = (Model<Float>) model;
            collector.submitModel(ShulkerLidOnlyModel.of(shulkerModel), openAmount, matrices, light, overlay, color,
                    spriteId, spriteGetter, outlineColor, crumblingOverlay);
            return;
        }

        collector.submitModel(model, animationState, matrices, light, overlay, color, spriteId, spriteGetter,
                outlineColor, crumblingOverlay);
    }
}
