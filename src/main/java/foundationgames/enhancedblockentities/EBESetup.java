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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.DyeColor;

public enum EBESetup {;
    private static final CopperChest[] COPPER_CHESTS = {
            new CopperChest("copper_chest", "copper", Blocks.COPPER_CHEST),
            new CopperChest("exposed_copper_chest", "copper_exposed", Blocks.EXPOSED_COPPER_CHEST),
            new CopperChest("weathered_copper_chest", "copper_weathered", Blocks.WEATHERED_COPPER_CHEST),
            new CopperChest("oxidized_copper_chest", "copper_oxidized", Blocks.OXIDIZED_COPPER_CHEST),
            new CopperChest("waxed_copper_chest", "copper", Blocks.WAXED_COPPER_CHEST),
            new CopperChest("waxed_exposed_copper_chest", "copper_exposed", Blocks.WAXED_EXPOSED_COPPER_CHEST),
            new CopperChest("waxed_weathered_copper_chest", "copper_weathered", Blocks.WAXED_WEATHERED_COPPER_CHEST),
            new CopperChest("waxed_oxidized_copper_chest", "copper_oxidized", Blocks.WAXED_OXIDIZED_COPPER_CHEST)
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
            ResourceUtil.addAliasedBlockSprite("entity/signs/" + signType, p);
            ResourceUtil.addAliasedBlockSprite("entity/signs/hanging/" + signType, p);
            ResourceUtil.addAliasedBlockSprite(
                    Identifier.withDefaultNamespace("gui/hanging_signs/" + signType),
                    EBEUtil.id("block/particle_hanging_sign_" + signType),
                    p);
        }
    }

    public static void setupRRPBells() {
        ResourceUtil.addBellBlockState(ResourceUtil.getPackForCompat());
        ResourceUtil.getBasePack().addSingleBlockSprite(Identifier.withDefaultNamespace("entity/bell/bell_body"));
    }

    public static void setupRRPBeds() {
        EBEPack p = ResourceUtil.getBasePack();
        EBEPack pCompat = ResourceUtil.getPackForCompat();

        for (DyeColor color : DyeColor.values()) {
            ResourceUtil.addBedBlockState(color, pCompat);
            ResourceUtil.addBedModels(color, p);
        }

        for (DyeColor color : DyeColor.values()) {
            ResourceUtil.addAliasedBlockSprite("entity/bed/" + color.getName(), p);
        }
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
        EnhancedBlockEntityRegistry.register(Blocks.CHEST, BlockEntityType.CHEST, BlockEntityRenderCondition.CHEST,
                BlockEntityRendererOverride.NO_OP
        );
        EnhancedBlockEntityRegistry.register(Blocks.TRAPPED_CHEST, BlockEntityType.TRAPPED_CHEST, BlockEntityRenderCondition.CHEST,
                BlockEntityRendererOverride.NO_OP
        );
        EnhancedBlockEntityRegistry.register(Blocks.ENDER_CHEST, BlockEntityType.ENDER_CHEST, BlockEntityRenderCondition.CHEST,
                BlockEntityRendererOverride.NO_OP
        );
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.CHEST);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.TRAPPED_CHEST);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.ENDER_CHEST);
        for (var copperChest : COPPER_CHESTS) {
            EnhancedBlockEntityRegistry.register(copperChest.block(), BlockEntityType.CHEST, BlockEntityRenderCondition.CHEST,
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
            EnhancedBlockEntityRegistry.register(sign, BlockEntityType.SIGN, BlockEntityRenderCondition.SIGN,
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
            EnhancedBlockEntityRegistry.register(sign, BlockEntityType.HANGING_SIGN, BlockEntityRenderCondition.SIGN,
                    BlockEntityRendererOverride.NO_OP
            );
            EnhancedBlockEntityRegistry.registerStaticModelBlock(sign);
        }
    }

    public static void setupBells() {
        EnhancedBlockEntityRegistry.register(Blocks.BELL, BlockEntityType.BELL, BlockEntityRenderCondition.BELL,
                BlockEntityRendererOverride.NO_OP
        );
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.BELL);
    }

    public static void setupBeds() {
        EnhancedBlockEntityRegistry.register(Blocks.BLACK_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.BLUE_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.BROWN_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.CYAN_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.GRAY_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.GREEN_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.LIGHT_BLUE_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.LIGHT_GRAY_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.LIME_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.MAGENTA_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.ORANGE_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.PINK_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.PURPLE_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.RED_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.WHITE_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.YELLOW_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.BLACK_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.BLUE_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.BROWN_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.CYAN_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.GRAY_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.GREEN_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.LIGHT_BLUE_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.LIGHT_GRAY_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.LIME_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.MAGENTA_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.ORANGE_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.PINK_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.PURPLE_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.RED_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.WHITE_BED);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.YELLOW_BED);
    }

    public static void setupShulkerBoxes() {
        EnhancedBlockEntityRegistry.register(Blocks.SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.WHITE_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.ORANGE_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.MAGENTA_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.LIGHT_BLUE_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.YELLOW_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.LIME_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.PINK_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.GRAY_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.LIGHT_GRAY_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.CYAN_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.PURPLE_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.BLUE_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.BROWN_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.GREEN_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.RED_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.BLACK_SHULKER_BOX, BlockEntityType.SHULKER_BOX,
                BlockEntityRenderCondition.SHULKER_BOX, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.WHITE_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.ORANGE_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.MAGENTA_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.LIGHT_BLUE_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.YELLOW_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.LIME_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.PINK_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.GRAY_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.LIGHT_GRAY_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.CYAN_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.PURPLE_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.BLUE_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.BROWN_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.GREEN_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.RED_SHULKER_BOX);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.BLACK_SHULKER_BOX);
    }

    public static void setupDecoratedPots() {
        EnhancedBlockEntityRegistry.register(Blocks.DECORATED_POT, BlockEntityType.DECORATED_POT,
                BlockEntityRenderCondition.DECORATED_POT, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.registerStaticModelBlock(Blocks.DECORATED_POT);
    }

    private record CopperChest(String blockName, String textureName, Block block) {}
}
