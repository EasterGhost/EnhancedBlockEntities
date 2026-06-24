package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.EnhancedBlockEntityRegistry;
import foundationgames.enhancedblockentities.client.render.BlockEntityRenderCondition;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
    @SuppressWarnings("null")
    @Inject(method = "tryExtractRenderState", at = @At("HEAD"), cancellable = true)
    private <E extends BlockEntity, S extends BlockEntityRenderState> void enhanced_bes$suppressStaticModelBlockEntityRenderer(
            E blockEntity, float tickDelta, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, boolean canRenderNameTag,
            CallbackInfoReturnable<S> cir) {
        if (this.enhanced_bes$shouldSuppressSignText(blockEntity)) {
            cir.setReturnValue(null);
            return;
        }

        if (blockEntity.getType() != BlockEntityTypes.SIGN
                && blockEntity.getType() != BlockEntityTypes.HANGING_SIGN
                && !this.enhanced_bes$shouldUseVanillaRenderer(blockEntity)
                && EnhancedBlockEntityRegistry.STATIC_MODEL_BLOCKS.contains(blockEntity.getBlockState().getBlock())) {
            cir.setReturnValue(null);
        }
    }

    private boolean enhanced_bes$shouldUseVanillaRenderer(BlockEntity blockEntity) {
        var entry = EnhancedBlockEntityRegistry.ENTITIES.get(blockEntity.getType());
        return entry != null
                && EnhancedBlockEntityRegistry.BLOCKS.contains(blockEntity.getBlockState().getBlock())
                && entry.getFirst().shouldRender(blockEntity);
    }

    private boolean enhanced_bes$shouldSuppressSignText(BlockEntity blockEntity) {
        if (blockEntity.getType() != BlockEntityTypes.SIGN && blockEntity.getType() != BlockEntityTypes.HANGING_SIGN) {
            return false;
        }

        if (!EnhancedBlockEntityRegistry.STATIC_MODEL_BLOCKS.contains(blockEntity.getBlockState().getBlock())) {
            return false;
        }

        return !BlockEntityRenderCondition.SIGN.shouldRender(blockEntity);
    }
}
