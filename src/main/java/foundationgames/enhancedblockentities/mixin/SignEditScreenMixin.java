package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.EnhancedBlockEntityRegistry;
import foundationgames.enhancedblockentities.client.gui.EBESignBlockRenderState;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignEditScreen.class)
public abstract class SignEditScreenMixin extends AbstractSignEditScreen {
    private static final float EBE_SIGN_BACKGROUND_SCALE = 93.75f;

    private SignEditScreenMixin(SignBlockEntity sign, boolean frontText, boolean filtered) {
        super(sign, frontText, filtered);
    }

    @Inject(method = "extractSignBackground", at = @At("HEAD"), cancellable = true)
    private void enhanced_bes$extractBakedModelSignBackground(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        BlockState state = this.sign.getBlockState();

        if (!EnhancedBlockEntities.CONFIG.renderEnhancedSigns || !EnhancedBlockEntityRegistry.BLOCKS.contains(state.getBlock())) {
            return;
        }

        float verticalOffset = 0.0f;
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
            verticalOffset = 5.0f / 16.0f;
        } else if (state.hasProperty(BlockStateProperties.ROTATION_16)) {
            state = state.setValue(BlockStateProperties.ROTATION_16, 0);
        }

        int center = this.width / 2;
        ((GuiGraphicsExtractorAccessor) graphics).enhanced_bes$getGuiRenderState().addPicturesInPictureState(new EBESignBlockRenderState(
                state, verticalOffset, center - 48, 66, center + 48, 168, EBE_SIGN_BACKGROUND_SCALE, null
        ));
        ci.cancel();
    }
}
