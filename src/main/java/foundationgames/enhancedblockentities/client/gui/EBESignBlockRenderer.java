package foundationgames.enhancedblockentities.client.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;

public class EBESignBlockRenderer extends PictureInPictureRenderer<EBESignBlockRenderState> {
    private static final int FULL_BRIGHT = 15728880;

    private final ModelBlockRenderer blockRenderer;

    public EBESignBlockRenderer(MultiBufferSource.BufferSource bufferSource) {
        super(bufferSource);
        this.blockRenderer = new ModelBlockRenderer(false, false, Minecraft.getInstance().getBlockColors());
    }

    @SuppressWarnings("null")
    @Override
    public Class<EBESignBlockRenderState> getRenderStateClass() {
        return EBESignBlockRenderState.class;
    }

    @SuppressWarnings("null")
    @Override
    protected void renderToTexture(EBESignBlockRenderState state, PoseStack matrices) {
        Minecraft client = Minecraft.getInstance();
        client.gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);

        var blockState = state.blockState();
        var model = client.getModelManager().getBlockStateModelSet().get(blockState);
        VertexConsumer consumer = this.bufferSource.getBuffer(Sheets.cutoutBlockSheet());

        matrices.pushPose();
        matrices.scale(1.0f, -1.0f, 1.0f);
        matrices.translate(-0.5f, state.verticalOffset() - 0.5f, 0.0f);

        BlockQuadOutput output = (x, y, z, quad, instance) -> {
            instance.setLightCoords(FULL_BRIGHT);
            if (x == 0.0f && y == 0.0f && z == 0.0f) {
                consumer.putBakedQuad(matrices.last(), quad, instance);
            } else {
                matrices.pushPose();
                matrices.translate(x, y, z);
                consumer.putBakedQuad(matrices.last(), quad, instance);
                matrices.popPose();
            }
        };

        this.blockRenderer.tesselateBlock(output, 0.0f, 0.0f, 0.0f, BlockAndTintGetter.EMPTY, BlockPos.ZERO, blockState, model, 42L);
        matrices.popPose();
    }

    @SuppressWarnings("null")
    @Override
    protected String getTextureLabel() {
        return "ebe_sign_block";
    }
}
