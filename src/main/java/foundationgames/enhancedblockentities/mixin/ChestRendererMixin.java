package foundationgames.enhancedblockentities.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.EnhancedBlockEntityRegistry;
import foundationgames.enhancedblockentities.client.model.ChestLidOnlyModel;
import foundationgames.enhancedblockentities.util.DateUtil;
import foundationgames.enhancedblockentities.util.duck.AppearanceStateHolder;
import foundationgames.enhancedblockentities.util.duck.EBEChestRenderState;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestRenderer.class)
public class ChestRendererMixin {
        @Inject(method = "extractRenderState(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V", at = @At("TAIL"))
        private void enhanced_bes$useConfiguredChristmasMaterial(BlockEntity blockEntity, ChestRenderState state,
                        float tickDelta,
                        Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, CallbackInfo ci) {
                if (EnhancedBlockEntities.CONFIG.renderEnhancedChests && blockEntity.getBlockState().is(Blocks.CHEST)) {
                        state.material = DateUtil.isChristmas()
                                        ? ChestRenderState.ChestMaterialType.CHRISTMAS
                                        : ChestRenderState.ChestMaterialType.REGULAR;
                } else if (EnhancedBlockEntities.CONFIG.renderEnhancedChests
                                && blockEntity.getBlockState().is(Blocks.TRAPPED_CHEST)) {
                        state.material = DateUtil.isChristmas()
                                        ? ChestRenderState.ChestMaterialType.CHRISTMAS
                                        : ChestRenderState.ChestMaterialType.TRAPPED;
                }

                boolean renderOnlyLid = blockEntity instanceof AppearanceStateHolder stateHolder
                                && stateHolder.getRenderState() > 0
                                && EnhancedBlockEntityRegistry.STATIC_MODEL_BLOCKS
                                                .contains(blockEntity.getBlockState().getBlock());
                ((EBEChestRenderState) state).enhanced_bes$renderOnlyLid(renderOnlyLid);
        }

        @SuppressWarnings("null")
        @Redirect(method = "submit(Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;IIILnet/minecraft/client/resources/model/sprite/SpriteId;Lnet/minecraft/client/resources/model/sprite/SpriteGetter;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
        private <S> void enhanced_bes$submitLidOnlyForStaticChest(SubmitNodeCollector collector, Model<S> model,
                        S animationState,
                        PoseStack matrices, int light, int overlay, int color, SpriteId spriteId,
                        SpriteGetter spriteGetter,
                        int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay,
                        ChestRenderState state,
                        PoseStack originalMatrices, SubmitNodeCollector originalCollector,
                        CameraRenderState cameraState) {
                if (((EBEChestRenderState) state).enhanced_bes$renderOnlyLid()
                                && model instanceof ChestModel chestModel
                                && animationState instanceof Float openAmount) {
                        collector.submitModel(ChestLidOnlyModel.of(chestModel), openAmount, matrices, light, overlay,
                                        color,
                                        spriteId, spriteGetter, outlineColor, crumblingOverlay);
                        return;
                }

                collector.submitModel(model, animationState, matrices, light, overlay, color, spriteId, spriteGetter,
                                outlineColor, crumblingOverlay);
        }
}
