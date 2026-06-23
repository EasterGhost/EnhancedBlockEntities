package foundationgames.enhancedblockentities.client.model;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.util.RandomSource;

import java.util.List;

public class EmptyBlockStateModel implements BlockStateModel {
    private final BlockStateModel particleSource;

    public EmptyBlockStateModel(BlockStateModel particleSource) {
        this.particleSource = particleSource;
    }

    @SuppressWarnings("null")
    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
    }

    @SuppressWarnings("null")
    @Override
    public Material.Baked particleMaterial() {
        return this.particleSource.particleMaterial();
    }

    @Override
    public int materialFlags() {
        return this.particleSource.materialFlags();
    }
}
