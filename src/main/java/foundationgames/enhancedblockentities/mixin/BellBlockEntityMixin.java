package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.util.duck.AppearanceStateHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BellBlockEntity.class)
public class BellBlockEntityMixin extends BlockEntity implements AppearanceStateHolder {
    @Unique private int enhanced_bes$modelState = 0;
    @Unique private int enhanced_bes$renderState = 0;

    public BellBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "clientTick", at = @At("TAIL"))
    private static void enhanced_bes$listenForStopRinging(Level world, BlockPos pos, BlockState state, BellBlockEntity blockEntity, CallbackInfo ci) {
        int mState = blockEntity.ticks > 0 ? 1 : 0;
        if (EnhancedBlockEntities.CONFIG.renderEnhancedBells && ((AppearanceStateHolder)blockEntity).getModelState() != mState) {
            ((AppearanceStateHolder)blockEntity).updateAppearanceState(mState, world, pos);
        }
    }

    @Override
    public int getModelState() {
        return enhanced_bes$modelState;
    }

    @Override
    public void setModelState(int state) {
        this.enhanced_bes$modelState = state;
    }

    @Override
    public int getRenderState() {
        return enhanced_bes$renderState;
    }

    @Override
    public void setRenderState(int state) {
        this.enhanced_bes$renderState = state;
    }
}
