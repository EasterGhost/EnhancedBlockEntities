package foundationgames.enhancedblockentities;

import foundationgames.enhancedblockentities.client.model.DynamicModelLoadingPlugin;
import foundationgames.enhancedblockentities.client.render.BlockEntityRenderCondition;
import foundationgames.enhancedblockentities.client.render.BlockEntityRendererOverride;
import foundationgames.enhancedblockentities.client.resource.EBEPack;
import foundationgames.enhancedblockentities.util.EBEUtil;
import foundationgames.enhancedblockentities.util.ResourceUtil;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.item.DyeColor;

public enum EBESetup {;
    private static final CopperChest[] COPPER_CHESTS = {
            new CopperChest("copper_chest", "copper", Blocks.COPPER_CHEST.weathering().unaffected()),
            new CopperChest("exposed_copper_chest", "copper_exposed", Blocks.COPPER_CHEST.weathering().exposed()),
            new CopperChest("weathered_copper_chest", "copper_weathered", Blocks.COPPER_CHEST.weathering().weathered()),
            new CopperChest("oxidized_copper_chest", "copper_oxidized", Blocks.COPPER_CHEST.weathering().oxidized()),
            new CopperChest("waxed_copper_chest", "copper", Blocks.COPPER_CHEST.waxed().unaffected()),
            new CopperChest("waxed_exposed_copper_chest", "copper_exposed", Blocks.COPPER_CHEST.waxed().exposed()),
            new CopperChest("waxed_weathered_copper_chest", "copper_weathered", Blocks.COPPER_CHEST.waxed().weathered()),
            new CopperChest("waxed_oxidized_copper_chest", "copper_oxidized", Blocks.COPPER_CHEST.waxed().oxidized())
    };

    private static final String[] COPPER_CHEST_TEXTURES = {
            "copper", "copper_left", "copper_right",
            "copper_exposed", "copper_exposed_left", "copper_exposed_right",
            "copper_weathered", "copper_weathered_left", "copper_weathered_right",
            "copper_oxidized", "copper_oxidized_left", "copper_oxidized_right"
    };

    public static void setupRRPChests() {
        EBEPack p = ResourceUtil.getPackForCompat();

        ResourceUtil.addChestBlockStates("chest", p);
        ResourceUtil.addChestBlockStates("trapped_chest", p);
        ResourceUtil.addChestBlockStates("christmas_chest", p);
        ResourceUtil.addSingleChestOnlyBlockStates("ender_chest", p);
        for (var copperChest : COPPER_CHESTS) {
            ResourceUtil.addChestBlockStates(copperChest.blockName(), p);
        }

        p = ResourceUtil.getBasePack();

        ResourceUtil.addSingleChestModels("normal", "chest", p);
        ResourceUtil.addDoubleChestModels("normal_left", "normal_right", "chest", p);
        ResourceUtil.addSingleChestModels("trapped", "trapped_chest", p);
        ResourceUtil.addDoubleChestModels("trapped_left", "trapped_right", "trapped_chest", p);
        ResourceUtil.addSingleChestModels("christmas", "christmas_chest", p);
        ResourceUtil.addDoubleChestModels("christmas_left", "christmas_right", "christmas_chest", p);
        ResourceUtil.addSingleChestModels("ender", "ender_chest", p);

        ResourceUtil.addChestItemDefinition("chest", "chest_center", true, p);
        ResourceUtil.addChestItemDefinition("trapped_chest", "trapped_chest_center", true, p);
        ResourceUtil.addChestItemDefinition("ender_chest", "ender_chest_center", false, p);
        for (var copperChest : COPPER_CHESTS) {
            ResourceUtil.addSingleChestModels(copperChest.textureName(), copperChest.blockName(), p);
            ResourceUtil.addDoubleChestModels(copperChest.textureName() + "_left", copperChest.textureName() + "_right",
                    copperChest.blockName(), p);
            ResourceUtil.addChestItemDefinition(copperChest.blockName(), copperChest.blockName() + "_center", false, p);
        }

        for (var texture : new String[] {
                "normal", "normal_left", "normal_right",
                "trapped", "trapped_left", "trapped_right",
                "christmas", "christmas_left", "christmas_right",
                "ender"
        }) {
            ResourceUtil.addAliasedBlockSprite("entity/chest/" + texture, p);
        }
        for (var texture : COPPER_CHEST_TEXTURES) {
            ResourceUtil.addAliasedBlockSprite("entity/chest/" + texture, p);
        }
    }

    public static void setupRRPSigns() {
        EBEPack p = ResourceUtil.getPackForCompat();

        ResourceUtil.addSignBlockStates("oak_sign", "oak_wall_sign", p);
        ResourceUtil.addSignBlockStates("birch_sign", "birch_wall_sign", p);
        ResourceUtil.addSignBlockStates("spruce_sign", "spruce_wall_sign", p);
        ResourceUtil.addSignBlockStates("jungle_sign", "jungle_wall_sign", p);
        ResourceUtil.addSignBlockStates("acacia_sign", "acacia_wall_sign", p);
        ResourceUtil.addSignBlockStates("dark_oak_sign", "dark_oak_wall_sign", p);
        ResourceUtil.addSignBlockStates("mangrove_sign", "mangrove_wall_sign", p);
        ResourceUtil.addSignBlockStates("cherry_sign", "cherry_wall_sign", p);
        ResourceUtil.addSignBlockStates("crimson_sign", "crimson_wall_sign", p);
        ResourceUtil.addSignBlockStates("warped_sign", "warped_wall_sign", p);
        ResourceUtil.addSignBlockStates("bamboo_sign", "bamboo_wall_sign", p);
        ResourceUtil.addSignBlockStates("pale_oak_sign", "pale_oak_wall_sign", p);

        ResourceUtil.addHangingSignBlockStates("oak_hanging_sign", "oak_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("birch_hanging_sign", "birch_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("spruce_hanging_sign", "spruce_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("jungle_hanging_sign", "jungle_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("acacia_hanging_sign", "acacia_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("dark_oak_hanging_sign", "dark_oak_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("mangrove_hanging_sign", "mangrove_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("cherry_hanging_sign", "cherry_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("crimson_hanging_sign", "crimson_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("warped_hanging_sign", "warped_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("bamboo_hanging_sign", "bamboo_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("pale_oak_hanging_sign", "pale_oak_wall_hanging_sign", p);

        p = ResourceUtil.getBasePack();

        ResourceUtil.addSignTypeModels("oak", p);
        ResourceUtil.addSignTypeModels("birch", p);
        ResourceUtil.addSignTypeModels("spruce", p);
        ResourceUtil.addSignTypeModels("jungle", p);
        ResourceUtil.addSignTypeModels("acacia", p);
        ResourceUtil.addSignTypeModels("dark_oak", p);
        ResourceUtil.addSignTypeModels("mangrove", p);
        ResourceUtil.addSignTypeModels("cherry", p);
        ResourceUtil.addSignTypeModels("crimson", p);
        ResourceUtil.addSignTypeModels("warped", p);
        ResourceUtil.addSignTypeModels("bamboo", p);
        ResourceUtil.addSignTypeModels("pale_oak", p);

        for (var signType : new String[] {
                "oak", "birch", "spruce", "jungle", "acacia", "dark_oak",
                "mangrove", "cherry", "crimson", "warped", "bamboo", "pale_oak"
        }) {
            ResourceUtil.addAliasedBlockSprite("block/" + signType + "_sign", p);
            ResourceUtil.addAliasedBlockSprite("block/" + signType + "_hanging_sign", p);
        }
    }

    public static void setupRRPBells() {
        ResourceUtil.addBellBlockState(ResourceUtil.getPackForCompat());
        ResourceUtil.getBasePack().addSingleBlockSprite(Identifier.withDefaultNamespace("entity/bell/bell_body"));
    }

    public static void setupRRPBeds() {
        // 26.2 beds are already vanilla block models and no longer have a block entity renderer.
    }

    public static void setupRRPShulkerBoxes() {
        EBEPack p = ResourceUtil.getBasePack();
        EBEPack pCompat = ResourceUtil.getPackForCompat();

        for (DyeColor color : EBEUtil.DEFAULTED_DYE_COLORS) {
            var id = color != null ? color.getName() + "_shulker_box" : "shulker_box";
            ResourceUtil.addShulkerBoxBlockStates(color, pCompat);
            ResourceUtil.addShulkerBoxModels(color, p);
            ResourceUtil.addParentModel("block/" + id, Identifier.withDefaultNamespace("item/" + id), p);
        }

        for (DyeColor color : EBEUtil.DEFAULTED_DYE_COLORS) {
            var texture = color != null ? "entity/shulker/shulker_" + color.getName() : "entity/shulker/shulker";
            ResourceUtil.addAliasedBlockSprite(texture, p);
        }
    }

    public static void setupRRPDecoratedPots() {
        EBEPack p = ResourceUtil.getBasePack();
        EBEPack pCompat = ResourceUtil.getPackForCompat();

        ResourceUtil.addDecoratedPotBlockState(pCompat);
        for (var patternKey : BuiltInRegistries.DECORATED_POT_PATTERN.registryKeySet()) {
            ResourceUtil.addDecoratedPotPatternModels(patternKey, p);
        }

        ResourceUtil.addAliasedBlockSprite("entity/decorated_pot/decorated_pot_base", p);
    }

    public static void setupResourceProviders() {
        ModelLoadingPlugin.register(new DynamicModelLoadingPlugin());
    }

    public static void setupChests() {
        EnhancedBlockEntityRegistry.register(Blocks.CHEST, BlockEntityTypes.CHEST, BlockEntityRenderCondition.CHEST,
                BlockEntityRendererOverride.NO_OP
        );
        EnhancedBlockEntityRegistry.register(Blocks.TRAPPED_CHEST, BlockEntityTypes.TRAPPED_CHEST, BlockEntityRenderCondition.CHEST,
                BlockEntityRendererOverride.NO_OP
        );
        EnhancedBlockEntityRegistry.register(Blocks.ENDER_CHEST, BlockEntityTypes.ENDER_CHEST, BlockEntityRenderCondition.CHEST,
                BlockEntityRendererOverride.NO_OP
        );
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.CHEST);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.TRAPPED_CHEST);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.ENDER_CHEST);
        for (var copperChest : COPPER_CHESTS) {
            EnhancedBlockEntityRegistry.register(copperChest.block(), BlockEntityTypes.CHEST, BlockEntityRenderCondition.CHEST,
                    BlockEntityRendererOverride.NO_OP);
            EnhancedBlockEntityRegistry.registerStaticModelBlock(copperChest.block());
        }
    }

    public static void setupSigns() {
        for (var sign : new Block[] {
                Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN,
                Blocks.BIRCH_SIGN, Blocks.BIRCH_WALL_SIGN,
                Blocks.SPRUCE_SIGN, Blocks.SPRUCE_WALL_SIGN,
                Blocks.JUNGLE_SIGN, Blocks.JUNGLE_WALL_SIGN,
                Blocks.ACACIA_SIGN, Blocks.ACACIA_WALL_SIGN,
                Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_WALL_SIGN,
                Blocks.MANGROVE_SIGN, Blocks.MANGROVE_WALL_SIGN,
                Blocks.CHERRY_SIGN, Blocks.CHERRY_WALL_SIGN,
                Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN,
                Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN,
                Blocks.BAMBOO_SIGN, Blocks.BAMBOO_WALL_SIGN,
                Blocks.PALE_OAK_SIGN, Blocks.PALE_OAK_WALL_SIGN
        }) {
            EnhancedBlockEntityRegistry.register(sign, BlockEntityTypes.SIGN, BlockEntityRenderCondition.SIGN,
                    BlockEntityRendererOverride.NO_OP
            );
            EnhancedBlockEntityRegistry.registerStaticModelBlock(sign);
        }

        for (var sign : new Block[] {
                Blocks.OAK_HANGING_SIGN, Blocks.OAK_WALL_HANGING_SIGN,
                Blocks.BIRCH_HANGING_SIGN, Blocks.BIRCH_WALL_HANGING_SIGN,
                Blocks.SPRUCE_HANGING_SIGN, Blocks.SPRUCE_WALL_HANGING_SIGN,
                Blocks.JUNGLE_HANGING_SIGN, Blocks.JUNGLE_WALL_HANGING_SIGN,
                Blocks.ACACIA_HANGING_SIGN, Blocks.ACACIA_WALL_HANGING_SIGN,
                Blocks.DARK_OAK_HANGING_SIGN, Blocks.DARK_OAK_WALL_HANGING_SIGN,
                Blocks.MANGROVE_HANGING_SIGN, Blocks.MANGROVE_WALL_HANGING_SIGN,
                Blocks.CHERRY_HANGING_SIGN, Blocks.CHERRY_WALL_HANGING_SIGN,
                Blocks.CRIMSON_HANGING_SIGN, Blocks.CRIMSON_WALL_HANGING_SIGN,
                Blocks.WARPED_HANGING_SIGN, Blocks.WARPED_WALL_HANGING_SIGN,
                Blocks.BAMBOO_HANGING_SIGN, Blocks.BAMBOO_WALL_HANGING_SIGN,
                Blocks.PALE_OAK_HANGING_SIGN, Blocks.PALE_OAK_WALL_HANGING_SIGN
        }) {
            EnhancedBlockEntityRegistry.register(sign, BlockEntityTypes.HANGING_SIGN, BlockEntityRenderCondition.SIGN,
                    BlockEntityRendererOverride.NO_OP
            );
            EnhancedBlockEntityRegistry.registerStaticModelBlock(sign);
        }
    }

    public static void setupBells() {
        EnhancedBlockEntityRegistry.register(Blocks.BELL, BlockEntityTypes.BELL, BlockEntityRenderCondition.BELL,
                BlockEntityRendererOverride.NO_OP
        );
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.BELL);
    }

    public static void setupBeds() {
        // 26.2 beds are already vanilla block models and no longer have a block entity renderer.
    }

    public static void setupShulkerBoxes() {
        EnhancedBlockEntityRegistry.register(Blocks.SHULKER_BOX, BlockEntityTypes.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.SHULKER_BOX);
        for (var shulkerBox : Blocks.DYED_SHULKER_BOX.asList()) {
            EnhancedBlockEntityRegistry.register(shulkerBox, BlockEntityTypes.SHULKER_BOX,
                    BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
            EnhancedBlockEntityRegistry.registerStaticModelBlock(shulkerBox);
        }
    }

    public static void setupDecoratedPots() {
        EnhancedBlockEntityRegistry.register(Blocks.DECORATED_POT, BlockEntityTypes.DECORATED_POT,
                BlockEntityRenderCondition.DECORATED_POT, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.DECORATED_POT);
    }

    private record CopperChest(String blockName, String textureName, Block block) {}
}
