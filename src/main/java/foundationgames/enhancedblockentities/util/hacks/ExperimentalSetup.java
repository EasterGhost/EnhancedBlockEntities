package foundationgames.enhancedblockentities.util.hacks;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.client.resource.EBEPack;
import foundationgames.enhancedblockentities.config.EBEConfig;
import foundationgames.enhancedblockentities.util.ResourceUtil;
import net.minecraft.server.packs.resources.ResourceManager;
import java.io.IOException;

public enum ExperimentalSetup {;
    private static ResourceManager RESOURCES;

    public static void setup() {
        EBEConfig config = EnhancedBlockEntities.CONFIG;

        if (config.renderEnhancedChests && config.experimentalChests) {
            try {
                if (RESOURCES != null) {
                    setupChests(RESOURCES);
                }
            } catch (IOException e) {
                EnhancedBlockEntities.LOG.error("Error loading experimental chests", e);
                config.experimentalChests = false;
                config.save();
            }
        }
        if (config.renderEnhancedSigns && config.experimentalSigns) {
            try {
                if (RESOURCES != null) {
                    setupSigns(RESOURCES);
                }
            } catch (IOException e) {
                EnhancedBlockEntities.LOG.error("Error loading experimental signs", e);
                config.experimentalSigns = false;
                config.save();
            }
        }
    }

    public static void setupChests(ResourceManager manager) throws IOException {
        EBEPack p = ResourceUtil.getTopLevelPack();

        ResourceHacks.addChestParticleTexture("chest", "entity/chest/normal", manager, p);
        ResourceHacks.addChestParticleTexture("trapped_chest", "entity/chest/trapped", manager, p);
        ResourceHacks.addChestParticleTexture("ender_chest", "entity/chest/ender", manager, p);
        ResourceHacks.addChestParticleTexture("christmas_chest", "entity/chest/christmas", manager, p);
        ResourceHacks.addChestParticleTexture("copper_chest", "entity/chest/copper", manager, p);
        ResourceHacks.addChestParticleTexture("exposed_copper_chest", "entity/chest/copper_exposed", manager, p);
        ResourceHacks.addChestParticleTexture("weathered_copper_chest", "entity/chest/copper_weathered", manager, p);
        ResourceHacks.addChestParticleTexture("oxidized_copper_chest", "entity/chest/copper_oxidized", manager, p);
        ResourceHacks.addChestParticleTexture("waxed_copper_chest", "entity/chest/copper", manager, p);
        ResourceHacks.addChestParticleTexture("waxed_exposed_copper_chest", "entity/chest/copper_exposed", manager, p);
        ResourceHacks.addChestParticleTexture("waxed_weathered_copper_chest", "entity/chest/copper_weathered", manager, p);
        ResourceHacks.addChestParticleTexture("waxed_oxidized_copper_chest", "entity/chest/copper_oxidized", manager, p);
    }

    public static void setupSigns(ResourceManager manager) throws IOException {
        EBEPack p = ResourceUtil.getTopLevelPack();

        ResourceHacks.addSignParticleTexture("oak", "block/oak_sign", manager, p);
        ResourceHacks.addSignParticleTexture("birch", "block/birch_sign", manager, p);
        ResourceHacks.addSignParticleTexture("spruce", "block/spruce_sign", manager, p);
        ResourceHacks.addSignParticleTexture("jungle", "block/jungle_sign", manager, p);
        ResourceHacks.addSignParticleTexture("acacia", "block/acacia_sign", manager, p);
        ResourceHacks.addSignParticleTexture("dark_oak", "block/dark_oak_sign", manager, p);
        ResourceHacks.addSignParticleTexture("mangrove", "block/mangrove_sign", manager, p);
        ResourceHacks.addSignParticleTexture("cherry", "block/cherry_sign", manager, p);
        ResourceHacks.addSignParticleTexture("crimson", "block/crimson_sign", manager, p);
        ResourceHacks.addSignParticleTexture("warped", "block/warped_sign", manager, p);
        ResourceHacks.addSignParticleTexture("bamboo", "block/bamboo_sign", manager, p);
        ResourceHacks.addSignParticleTexture("pale_oak", "block/pale_oak_sign", manager, p);
    }

    public static void cacheResources(ResourceManager resources) {
        RESOURCES = resources;
    }
}
