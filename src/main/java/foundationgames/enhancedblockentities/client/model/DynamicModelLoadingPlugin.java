package foundationgames.enhancedblockentities.client.model;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.util.DateUtil;
import foundationgames.enhancedblockentities.util.EBEUtil;
import com.mojang.math.Quadrant;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicModelLoadingPlugin implements ModelLoadingPlugin {
    @SuppressWarnings("null")
    @Override
    public void initialize(Context context) {
        context.modifyBlockModelOnLoad().register((model, ctx) -> {
            BlockState state = ctx.state();
            Block block = state.getBlock();

            if (EnhancedBlockEntities.CONFIG.renderEnhancedChests && (block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST)) {
                return this.wrapChest(model, state, block == Blocks.TRAPPED_CHEST ? "trapped_chest" : "chest", true);
            }
            if (EnhancedBlockEntities.CONFIG.renderEnhancedChests && this.isVanillaCopperChest(block)) {
                return this.wrapChest(model, state, BuiltInRegistries.BLOCK.getKey(block).getPath(), false);
            }
            if (EnhancedBlockEntities.CONFIG.renderEnhancedChests && block == Blocks.ENDER_CHEST) {
                return this.wrapSingleStateHolderModel(model, this.unbaked(state, "ender_chest_center_trunk"), 0,
                        DynamicModelEffects.CHEST);
            }
            if (EnhancedBlockEntities.CONFIG.renderEnhancedShulkerBoxes && this.isVanillaShulkerBox(block)) {
                String path = BuiltInRegistries.BLOCK.getKey(block).getPath();
                return this.wrapSingleStateHolderModel(model, this.unbaked(state, path + "_bottom"), 0,
                        DynamicModelEffects.SHULKER_BOX);
            }
            if (EnhancedBlockEntities.CONFIG.renderEnhancedBells && block == Blocks.BELL) {
                return this.wrapSingleStateHolderModel(model, this.unbakedBellSupport(state), 0, DynamicModelEffects.BELL);
            }
            if (EnhancedBlockEntities.CONFIG.renderEnhancedDecoratedPots && block == Blocks.DECORATED_POT) {
                return this.wrapDecoratedPot(model, state);
            }

            return model;
        });
    }

    private BlockStateModel.UnbakedRoot wrapChest(BlockStateModel.UnbakedRoot fallback, BlockState state, String chestName,
            boolean christmas) {
        ChestType type = ChestType.SINGLE;
        if (state.hasProperty(BlockStateProperties.CHEST_TYPE)) {
            type = state.getValue(BlockStateProperties.CHEST_TYPE);
        }

        String suffix = switch (type) {
            case LEFT -> "_left";
            case RIGHT -> "_right";
            case SINGLE -> "_center";
        };

        String base = chestName + suffix;
        if (!christmas) {
            return new WrappingUnbakedRoot(fallback, List.of(this.unbaked(state, base + "_trunk")),
                    (fallbackModel, models) -> new DynamicBlockStateModel(fallbackModel, new BlockStateModel[] {
                            fallbackModel,
                            models[0]
                    }, 0, DynamicModelEffects.CHEST));
        }

        String christmasBase = "christmas_chest" + suffix;
        return new WrappingUnbakedRoot(fallback, List.of(
                this.unbaked(state, base + "_trunk"),
                this.unbaked(state, christmasBase),
                this.unbaked(state, christmasBase + "_trunk")
        ), (fallbackModel, models) -> new DynamicBlockStateModel(fallbackModel, new BlockStateModel[] {
                fallbackModel,
                models[0],
                models[1],
                models[2]
        }, () -> DateUtil.isChristmas() ? 2 : 0,
                modelState -> DateUtil.isChristmas() ? modelState + 2 : modelState,
                DynamicModelEffects.CHEST));
    }

    private boolean isVanillaCopperChest(Block block) {
        return Blocks.COPPER_CHEST.asList().contains(block);
    }

    private BlockStateModel.UnbakedRoot wrapSingleStateHolderModel(BlockStateModel.UnbakedRoot fallback,
            BlockStateModel.Unbaked activeModel, int particleModelIndex,
            DynamicModelEffects effects) {
        return new WrappingUnbakedRoot(fallback, List.of(activeModel), (fallbackModel, models) ->
                new DynamicBlockStateModel(fallbackModel, new BlockStateModel[] {
                        fallbackModel,
                        models[0]
                }, particleModelIndex, effects));
    }

    private boolean isVanillaShulkerBox(Block block) {
        return block == Blocks.SHULKER_BOX || Blocks.DYED_SHULKER_BOX.asList().contains(block);
    }

    @SuppressWarnings("null")
    private BlockStateModel.UnbakedRoot wrapDecoratedPot(BlockStateModel.UnbakedRoot fallback, BlockState state) {
        List<BlockStateModel.Unbaked> dependencies = new ArrayList<>();
        Map<ResourceKey<DecoratedPotPattern>, EnumMap<Direction, Integer>> patternIndexes = new HashMap<>();

        for (var patternKey : BuiltInRegistries.DECORATED_POT_PATTERN.registryKeySet()) {
            var sideModels = new EnumMap<Direction, Integer>(Direction.class);
            for (var dir : EBEUtil.HORIZONTAL_DIRECTIONS) {
                sideModels.put(dir, dependencies.size());
                dependencies.add(this.unbaked(state, patternKey.identifier().getPath() + "_" + dir.getName()));
            }
            patternIndexes.put(patternKey, sideModels);
        }

        return new WrappingUnbakedRoot(fallback, dependencies, (fallbackModel, models) -> {
            Map<ResourceKey<DecoratedPotPattern>, EnumMap<Direction, BlockStateModel>> patterns = new HashMap<>();

            for (var entry : patternIndexes.entrySet()) {
                var bakedSides = new EnumMap<Direction, BlockStateModel>(Direction.class);
                for (var side : entry.getValue().entrySet()) {
                    bakedSides.put(side.getKey(), models[side.getValue()]);
                }
                patterns.put(entry.getKey(), bakedSides);
            }

            return new DecoratedPotBlockStateModel(fallbackModel, new EmptyBlockStateModel(fallbackModel),
                    patterns, DynamicModelEffects.DECORATED_POT);
        });
    }

    @SuppressWarnings("null")
    private BlockStateModel.Unbaked unbakedBellSupport(BlockState state) {
        BellAttachType attachment = state.getValue(BellBlock.ATTACHMENT);
        String model = switch (attachment) {
            case DOUBLE_WALL -> "bell_between_walls";
            case CEILING -> "bell_ceiling";
            case FLOOR -> "bell_floor";
            case SINGLE_WALL -> "bell_wall";
        };

        int rot = EBEUtil.angle(state.getValue(BellBlock.FACING)) + 90;
        if (attachment == BellAttachType.CEILING || attachment == BellAttachType.FLOOR) {
            rot += 90;
        }

        var variant = new Variant(Identifier.withDefaultNamespace("block/" + model)).withYRot(this.quadrant(rot));
        return new SingleVariant.Unbaked(variant);
    }

    @SuppressWarnings("null")
    private BlockStateModel.Unbaked unbaked(BlockState state, String modelPath) {
        return new SingleVariant.Unbaked(this.variant(state, modelPath));
    }

    @SuppressWarnings("null")
    private Variant variant(BlockState state, String modelPath) {
        Variant variant = new Variant(Identifier.withDefaultNamespace("block/" + modelPath));

        if (state.hasProperty(BlockStateProperties.FACING)) {
            var facing = state.getValue(BlockStateProperties.FACING);
            if (facing == Direction.DOWN) {
                variant = variant.withXRot(Quadrant.R180);
            } else if (facing != Direction.UP) {
                variant = variant.withXRot(Quadrant.R90).withYRot(this.quadrant(EBEUtil.angle(facing) + 180));
            }
        } else if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            variant = variant.withYRot(this.quadrant(EBEUtil.angle(state.getValue(BlockStateProperties.HORIZONTAL_FACING))));
        }

        return variant;
    }

    private Quadrant quadrant(int degrees) {
        return switch (Math.floorMod(degrees, 360)) {
            case 90 -> Quadrant.R90;
            case 180 -> Quadrant.R180;
            case 270 -> Quadrant.R270;
            default -> Quadrant.R0;
        };
    }

    @FunctionalInterface
    private interface BakedModelFactory {
        BlockStateModel create(BlockStateModel fallback, BlockStateModel[] models);
    }

    private record WrappingUnbakedRoot(
            BlockStateModel.UnbakedRoot fallback,
            List<BlockStateModel.Unbaked> dependencies,
            BakedModelFactory factory
    ) implements BlockStateModel.UnbakedRoot {
        @SuppressWarnings("null")
        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            this.fallback.resolveDependencies(resolver);
            for (var dependency : this.dependencies) {
                dependency.resolveDependencies(resolver);
            }
        }

        @SuppressWarnings("null")
        @Override
        public BlockStateModel bake(BlockState state, ModelBaker baker) {
            BlockStateModel fallbackModel = this.fallback.bake(state, baker);
            BlockStateModel[] bakedModels = new BlockStateModel[this.dependencies.size()];

            for (int i = 0; i < this.dependencies.size(); i++) {
                bakedModels[i] = this.dependencies.get(i).bake(baker);
            }

            return this.factory.create(fallbackModel, bakedModels);
        }

        @SuppressWarnings("null")
        @Override
        public Object visualEqualityGroup(BlockState state) {
            return this.fallback.visualEqualityGroup(state);
        }
    }
}
