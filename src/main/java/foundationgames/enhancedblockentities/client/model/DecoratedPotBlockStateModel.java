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
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class DecoratedPotBlockStateModel extends WrapperBlockStateModel {
    private static final Direction[] DECORATION_SIDES = {
            Direction.NORTH,
            Direction.WEST,
            Direction.EAST,
            Direction.SOUTH
    };

    private final BlockStateModel shakingModel;
    private final Map<ResourceKey<DecoratedPotPattern>, EnumMap<Direction, BlockStateModel>> patternModels;
    private final DynamicModelEffects effects;

    public DecoratedPotBlockStateModel(
            BlockStateModel baseModel,
            BlockStateModel shakingModel,
            Map<ResourceKey<DecoratedPotPattern>, EnumMap<Direction, BlockStateModel>> patternModels,
            DynamicModelEffects effects
    ) {
        super(baseModel);
        this.shakingModel = shakingModel;
        this.patternModels = patternModels;
        this.effects = effects;
    }

    @SuppressWarnings("null")
    @Override
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter view, BlockPos pos, BlockState state, RandomSource random, Predicate<Direction> cullTest) {
        TriState ambientOcclusion = TriState.of(this.effects.ambientOcclusion());
        emitter.pushTransform(quad -> {
            quad.ambientOcclusion(ambientOcclusion);
            return true;
        });
        try {
            for (var model : this.selectModels(view, pos)) {
                this.emitModel(model, emitter, view, pos, state, random, cullTest);
            }
        } finally {
            emitter.popTransform();
        }
    }

    @SuppressWarnings("null")
    @Override
    public Object createGeometryKey(BlockAndTintGetter view, BlockPos pos, BlockState state, RandomSource random) {
        var models = this.selectModels(view, pos);
        var key = new ArrayList<>(models.size());

        for (var model : models) {
            key.add(this.createGeometryKey(model, view, pos, state, random));
        }

        return key;
    }

    @SuppressWarnings("null")
    @Override
    public Material.Baked particleMaterial(BlockAndTintGetter view, BlockPos pos, BlockState state) {
        if (this.wrapped instanceof FabricBlockStateModel model) {
            return model.particleMaterial(view, pos, state);
        }

        return this.wrapped.particleMaterial();
    }

    @Override
    public int materialFlags() {
        int flags = super.materialFlags() | this.shakingModel.materialFlags();
        for (var sideModels : this.patternModels.values()) {
            for (var model : sideModels.values()) {
                flags |= model.materialFlags();
            }
        }
        return flags;
    }

    @Override
    public boolean hasMaterialFlag(int flag) {
        return (this.materialFlags() & flag) != 0;
    }

    @SuppressWarnings("null")
    private void emitModel(BlockStateModel model, QuadEmitter emitter, BlockAndTintGetter view, BlockPos pos,
            BlockState state, RandomSource random, Predicate<Direction> cullTest) {
        if (model instanceof FabricBlockStateModel fabricModel) {
            fabricModel.emitQuads(emitter, view, pos, state, random, cullTest);
        } else {
            this.emitVanillaModel(model, emitter, random, cullTest);
        }
    }

    @SuppressWarnings("null")
    private Object createGeometryKey(BlockStateModel model, BlockAndTintGetter view, BlockPos pos,
            BlockState state, RandomSource random) {
        if (model instanceof FabricBlockStateModel fabricModel) {
            return fabricModel.createGeometryKey(view, pos, state, random);
        }

        return model;
    }

    @SuppressWarnings("null")
    private void emitVanillaModel(BlockStateModel model, QuadEmitter emitter, RandomSource random,
            Predicate<Direction> cullTest) {
        var parts = new ArrayList<BlockStateModelPart>();
        model.collectParts(random, parts);
        for (var part : parts) {
            if (part instanceof FabricBlockStateModelPart fabricPart) {
                fabricPart.emitQuads(emitter, cullTest);
            }
        }
    }

    @SuppressWarnings("null")
    private List<BlockStateModel> selectModels(BlockAndTintGetter view, BlockPos pos) {
        BlockEntity blockEntity = view.getBlockEntity(pos);

        if (blockEntity instanceof AppearanceStateHolder stateHolder && stateHolder.getModelState() > 0) {
            return List.of(this.shakingModel);
        }

        if (blockEntity instanceof DecoratedPotBlockEntity pot) {
            var decorations = pot.getDecorations();
            return List.of(
                    this.wrapped,
                    this.patternModel(decorations.back(), Direction.NORTH),
                    this.patternModel(decorations.left(), Direction.WEST),
                    this.patternModel(decorations.right(), Direction.EAST),
                    this.patternModel(decorations.front(), Direction.SOUTH)
            );
        }

        var models = new ArrayList<BlockStateModel>(5);
        models.add(this.wrapped);
        for (var dir : DECORATION_SIDES) {
            models.add(this.patternModel(Optional.empty(), dir));
        }
        return models;
    }

    @SuppressWarnings("null")
    private BlockStateModel patternModel(Optional<Item> item, Direction side) {
        var pattern = item.map(DecoratedPotPatterns::getPatternFromItem).orElse(DecoratedPotPatterns.BLANK);
        var sideModels = this.patternModels.get(pattern);

        if (sideModels == null) {
            sideModels = this.patternModels.get(DecoratedPotPatterns.BLANK);
        }

        if (sideModels != null && sideModels.containsKey(side)) {
            return sideModels.get(side);
        }

        return this.wrapped;
    }
}
