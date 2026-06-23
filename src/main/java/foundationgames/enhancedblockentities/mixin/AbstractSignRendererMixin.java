package foundationgames.enhancedblockentities.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import foundationgames.enhancedblockentities.EnhancedBlockEntityRegistry;
import foundationgames.enhancedblockentities.util.duck.EBESignRenderState;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignRenderer.class)
public abstract class AbstractSignRendererMixin {
    @Unique private static final ThreadLocal<Boolean> enhanced_bes$skipSignBody = ThreadLocal.withInitial(() -> false);

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/level/block/entity/SignBlockEntity;Lnet/minecraft/client/renderer/blockentity/state/SignRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
            at = @At("RETURN")
    )
    private void enhanced_bes$markStaticSignModel(SignBlockEntity blockEntity, SignRenderState state, float tickDelta,
                                                  Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay,
                                                  CallbackInfo ci) {
        ((EBESignRenderState) state).enhanced_bes$setSkipSignBody(
                EnhancedBlockEntityRegistry.STATIC_MODEL_BLOCKS.contains(blockEntity.getBlockState().getBlock()));
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/blockentity/state/SignRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At("HEAD")
    )
    private void enhanced_bes$beforeSubmit(SignRenderState state, PoseStack matrices, SubmitNodeCollector collector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        enhanced_bes$skipSignBody.set(((EBESignRenderState) state).enhanced_bes$skipSignBody());
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/blockentity/state/SignRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At("RETURN")
    )
    private void enhanced_bes$afterSubmit(SignRenderState state, PoseStack matrices, SubmitNodeCollector collector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        enhanced_bes$skipSignBody.remove();
    }

    @Inject(method = "submitSign", at = @At("HEAD"), cancellable = true)
    private void enhanced_bes$skipSignModel(PoseStack matrices, int light, WoodType woodType, Model.Simple model,
                                            ModelFeatureRenderer.CrumblingOverlay crumblingOverlay,
                                            SubmitNodeCollector collector, CallbackInfo ci) {
        if (enhanced_bes$skipSignBody.get()) {
            ci.cancel();
        }
    }
}
