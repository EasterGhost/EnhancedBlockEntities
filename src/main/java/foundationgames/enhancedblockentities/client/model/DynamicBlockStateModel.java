package foundationgames.enhancedblockentities.client.model;

import foundationgames.enhancedblockentities.util.duck.AppearanceStateHolder;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.model.FabricBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.model.FabricBlockStateModelPart;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.IntUnaryOperator;
import java.util.function.IntSupplier;

public class DynamicBlockStateModel extends WrapperBlockStateModel {
    private final BlockStateModel[] models;
    private final IntSupplier particleModelIndex;
    private final IntUnaryOperator modelStateMapper;
    private final DynamicModelEffects effects;

    public DynamicBlockStateModel(BlockStateModel fallback, BlockStateModel[] models, int particleModelIndex) {
        this(fallback, models, particleModelIndex, IntUnaryOperator.identity(), DynamicModelEffects.DEFAULT);
    }

    public DynamicBlockStateModel(BlockStateModel fallback, BlockStateModel[] models, int particleModelIndex, DynamicModelEffects effects) {
        this(fallback, models, particleModelIndex, IntUnaryOperator.identity(), effects);
    }

    public DynamicBlockStateModel(BlockStateModel fallback, BlockStateModel[] models, int particleModelIndex, IntUnaryOperator modelStateMapper) {
        this(fallback, models, particleModelIndex, modelStateMapper, DynamicModelEffects.DEFAULT);
    }

    public DynamicBlockStateModel(BlockStateModel fallback, BlockStateModel[] models, int particleModelIndex,
            IntUnaryOperator modelStateMapper, DynamicModelEffects effects) {
        this(fallback, models, () -> particleModelIndex, modelStateMapper, effects);
    }

    public DynamicBlockStateModel(BlockStateModel fallback, BlockStateModel[] models, IntSupplier particleModelIndex,
            IntUnaryOperator modelStateMapper, DynamicModelEffects effects) {
        super(fallback);
        this.models = models;
        this.particleModelIndex = particleModelIndex;
        this.modelStateMapper = modelStateMapper;
        this.effects = effects;
    }

    @SuppressWarnings("null")
    @Override
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter view, BlockPos pos, BlockState state, RandomSource random, Predicate<Direction> cullTest) {
        var model = this.selectModel(view, pos);
        if (model instanceof FabricBlockStateModel fabricModel) {
            this.emitModel(emitter, fabricModel, view, pos, state, random, cullTest);
        } else if (model == this.wrapped) {
            super.emitQuads(emitter, view, pos, state, random, cullTest);
        } else {
            this.emitVanillaModel(emitter, model, random, cullTest);
        }
    }

    @SuppressWarnings("null")
    @Override
    public Object createGeometryKey(BlockAndTintGetter view, BlockPos pos, BlockState state, RandomSource random) {
        var model = this.selectModel(view, pos);
        if (model instanceof FabricBlockStateModel fabricModel) {
            return Arrays.asList(this.getSelectedIndex(view, pos), fabricModel.createGeometryKey(view, pos, state, random));
        }
        if (model == this.wrapped) {
            return Arrays.asList(this.getSelectedIndex(view, pos), super.createGeometryKey(view, pos, state, random));
        }
        return Arrays.asList(this.getSelectedIndex(view, pos), model);
    }

    @SuppressWarnings("null")
    @Override
    public Material.Baked particleMaterial(BlockAndTintGetter view, BlockPos pos, BlockState state) {
        int particleModelIndex = this.particleModelIndex.getAsInt();
        if (particleModelIndex >= 0 && particleModelIndex < models.length) {
            var model = models[particleModelIndex];
            if (model instanceof FabricBlockStateModel fabricModel) {
                return fabricModel.particleMaterial(view, pos, state);
            }
            return model.particleMaterial();
        }

        return super.particleMaterial(view, pos, state);
    }

    @Override
    public int materialFlags() {
        int flags = super.materialFlags();
        for (var model : this.models) {
            if (model != null) {
                flags |= model.materialFlags();
            }
        }
        return flags;
    }

    @Override
    public boolean hasMaterialFlag(int flag) {
        return (this.materialFlags() & flag) != 0;
    }

    private BlockStateModel selectModel(BlockAndTintGetter view, BlockPos pos) {
        int index = this.getSelectedIndex(view, pos);
        if (index >= 0 && index < models.length && models[index] != null) {
            return models[index];
        }

        return this.wrapped;
    }

    @SuppressWarnings("null")
    private int getSelectedIndex(BlockAndTintGetter view, BlockPos pos) {
        BlockEntity blockEntity = view.getBlockEntity(pos);
        if (blockEntity instanceof AppearanceStateHolder stateHolder) {
            return this.modelStateMapper.applyAsInt(stateHolder.getModelState());
        }

        return this.modelStateMapper.applyAsInt(0);
    }

    @SuppressWarnings("null")
    private void emitModel(QuadEmitter emitter, FabricBlockStateModel model, BlockAndTintGetter view, BlockPos pos,
            BlockState state, RandomSource random, Predicate<Direction> cullTest) {
        TriState ambientOcclusion = TriState.of(this.effects.ambientOcclusion());
        emitter.pushTransform(quad -> {
            quad.ambientOcclusion(ambientOcclusion);
            return true;
        });
        try {
            model.emitQuads(emitter, view, pos, state, random, cullTest);
        } finally {
            emitter.popTransform();
        }
    }

    @SuppressWarnings("null")
    private void emitVanillaModel(QuadEmitter emitter, BlockStateModel model, RandomSource random,
            Predicate<Direction> cullTest) {
        TriState ambientOcclusion = TriState.of(this.effects.ambientOcclusion());
        emitter.pushTransform(quad -> {
            quad.ambientOcclusion(ambientOcclusion);
            return true;
        });
        try {
            var parts = new ArrayList<BlockStateModelPart>();
            model.collectParts(random, parts);
            for (var part : parts) {
                if (part instanceof FabricBlockStateModelPart fabricPart) {
                    fabricPart.emitQuads(emitter, cullTest);
                }
            }
        } finally {
            emitter.popTransform();
        }
    }
}
