package foundationgames.enhancedblockentities.client.render.entity;

import foundationgames.enhancedblockentities.client.render.BlockEntityRendererOverride;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class ChestBlockEntityRendererOverride extends BlockEntityRendererOverride {
    @Override
    public void render(BlockEntityRenderer<BlockEntity, ?> renderer, BlockEntity blockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {}

    @SuppressWarnings("null")
    public static LidBlockEntity getLidAnimationHolder(BlockEntity blockEntity, float tickDelta) {
        LidBlockEntity chest = (LidBlockEntity)blockEntity;

        BlockState state = blockEntity.getBlockState();
        if (state.hasProperty(BlockStateProperties.CHEST_TYPE) && state.getValue(BlockStateProperties.CHEST_TYPE) != ChestType.SINGLE) {
            BlockEntity neighbor = null;
            BlockPos pos = blockEntity.getBlockPos();
            Direction facing = state.getValue(ChestBlock.FACING);
            switch (state.getValue(BlockStateProperties.CHEST_TYPE)) {
                case LEFT -> neighbor = blockEntity.getLevel().getBlockEntity(pos.relative(facing.getClockWise()));
                case RIGHT -> neighbor = blockEntity.getLevel().getBlockEntity(pos.relative(facing.getCounterClockWise()));
                case SINGLE -> {
                }
            }
            if (neighbor instanceof LidBlockEntity) {
                float nAnim = ((LidBlockEntity)neighbor).getOpenNess(tickDelta);
                if (nAnim > chest.getOpenNess(tickDelta)) {
                    chest = ((LidBlockEntity)neighbor);
                }
            }
        }

        return chest;
    }

    @Override
    public void onModelsReload() {
    }
}
