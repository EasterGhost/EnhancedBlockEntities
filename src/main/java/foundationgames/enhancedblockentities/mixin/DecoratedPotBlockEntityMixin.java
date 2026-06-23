package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.util.WorldUtil;
import foundationgames.enhancedblockentities.util.duck.AppearanceStateHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DecoratedPotBlockEntity.class)
public class DecoratedPotBlockEntityMixin implements AppearanceStateHolder {
    @Unique private int enhanced_bes$modelState = 0;
    @Unique private int enhanced_bes$renderState = 0;

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void enhanced_bes$updateChunkOnPatternsLoaded(ValueInput input, CallbackInfo ci) {
        var self = (DecoratedPotBlockEntity)(Object)this;
        var level = self.getLevel();

        if (level != null && level.isClientSide()) {
            WorldUtil.rebuildChunk(level, self.getBlockPos());
        }
    }

    @Inject(method = "triggerEvent", at = @At("TAIL"))
    private void enhanced_bes$updateOnWobble(int type, int data, CallbackInfoReturnable<Boolean> cir) {
        var self = (DecoratedPotBlockEntity)(Object)this;
        var level = self.getLevel();

        if (!cir.getReturnValueZ() || level == null || !level.isClientSide() || self.lastWobbleStyle == null) {
            return;
        }

        this.updateAppearanceState(1, level, self.getBlockPos());

        @SuppressWarnings("null")
        long endTime = self.wobbleStartedAtTick + self.lastWobbleStyle.duration;
        WorldUtil.scheduleTimed(level, endTime, () -> {
            Level currentLevel = self.getLevel();
            if (currentLevel != null && currentLevel.getGameTime() >= endTime) {
                this.updateAppearanceState(0, currentLevel, self.getBlockPos());
            }
        });
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
