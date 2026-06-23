package foundationgames.enhancedblockentities.util;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Direction;

import java.io.IOException;
import java.nio.file.Files;

import org.jspecify.annotations.NonNull;

public enum EBEUtil {;
    // Contains all dye colors, and null
    public static final DyeColor[] DEFAULTED_DYE_COLORS;
    // All directions except up and down
    public static final Direction[] HORIZONTAL_DIRECTIONS;

    static {
        var dColors = DyeColor.values();
        DEFAULTED_DYE_COLORS = new DyeColor[dColors.length + 1];
        System.arraycopy(dColors, 0, DEFAULTED_DYE_COLORS, 0, dColors.length);

        HORIZONTAL_DIRECTIONS = new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    }

    public static int angle(Direction dir) {
        return switch (dir) {
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            default -> 0;
        };
    }

    public static boolean isVanillaResourcePack(PackResources pack) {
        return (pack instanceof VanillaPackResources) ||
                // Terrible quilt compat hack
                ("org.quiltmc.qsl.resource.loader.api.GroupResourcePack$Wrapped".equals(pack.getClass().getName()));
    }

    public static Identifier id(@NonNull String path) {
        return Identifier.fromNamespaceAndPath(EnhancedBlockEntities.NAMESPACE, path);
    }

    public static final String DUMP_FOLDER_NAME = "enhanced_bes_dump";

    public static void dumpResources() throws IOException {
        var path = FabricLoader.getInstance().getGameDir().resolve(DUMP_FOLDER_NAME);

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        ResourceUtil.dumpAllPacks(path);
    }
}
