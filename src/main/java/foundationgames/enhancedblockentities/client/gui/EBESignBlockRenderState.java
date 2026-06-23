package foundationgames.enhancedblockentities.client.gui;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.world.level.block.state.BlockState;

public record EBESignBlockRenderState(
        BlockState blockState,
        float verticalOffset,
        int x0,
        int y0,
        int x1,
        int y1,
        float scale,
        ScreenRectangle scissorArea,
        ScreenRectangle bounds
) implements PictureInPictureRenderState {
    public EBESignBlockRenderState(BlockState blockState, float verticalOffset, int x0, int y0, int x1, int y1,
            float scale, ScreenRectangle scissorArea) {
        this(blockState, verticalOffset, x0, y0, x1, y1, scale, scissorArea,
                PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
    }
}
