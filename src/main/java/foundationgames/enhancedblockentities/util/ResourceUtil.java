package foundationgames.enhancedblockentities.util;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.client.resource.EBEPack;
import foundationgames.enhancedblockentities.client.resource.template.TemplateProvider;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ResourceUtil {;
    private static EBEPack BASE_PACK;
    private static EBEPack TOP_LEVEL_PACK;

    @SuppressWarnings("null")
    private static Identifier vanillaId(String path) {
        return Identifier.withDefaultNamespace(path);
    }

    @SuppressWarnings("null")
    private static Identifier id(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    public static Identifier spriteAlias(Identifier source) {
        return id(EnhancedBlockEntities.NAMESPACE, source.getPath());
    }

    public static String spriteAlias(String path) {
        return spriteAlias(vanillaId(path)).toString();
    }

    public static void addAliasedBlockSprite(String sourcePath, EBEPack pack) {
        addAliasedBlockSprite(vanillaId(sourcePath), pack);
    }

    public static void addAliasedBlockSprite(Identifier source, EBEPack pack) {
        pack.addSingleBlockSprite(source, spriteAlias(source));
    }

    public static void addAliasedBlockSprite(Identifier source, Identifier spriteId, EBEPack pack) {
        pack.addSingleBlockSprite(source, spriteId);
    }

    public static void addChestItemDefinition(String chestName, String centerChest, boolean hasChristmas, EBEPack pack) {
        pack.addTemplateResource(vanillaId("items/" + chestName + ".json"),
                t -> t.load(hasChristmas ? "item/chest_item.json" : "item/chest_item_no_christmas.json",
                        d -> d.def("chest", centerChest)));
    }

    public static void addBedItemDefinition(String bedColor, EBEPack pack) {
        pack.addTemplateResource(vanillaId("items/" + bedColor + "_bed.json"),
                t -> t.load("item/bed.json",
                        d -> d.def("head", bedColor + "_bed_head").def("foot", bedColor + "_bed_foot")));

        pack.addTemplateResource(vanillaId("models/item/" + bedColor + "_bed_head.json"),
                t -> t.load("model/bed_head_item.json", d -> d.def("bed", bedColor)));
        pack.addTemplateResource(vanillaId("models/item/" + bedColor + "_bed_foot.json"),
                t -> t.load("model/bed_foot_item.json", d -> d.def("bed", spriteAlias("entity/bed/" + bedColor))));
    }

    private static String list(String ... els) {
        return String.join(",", els);
    }

    private static String kv(String k, String v) {
        return String.format("\"" + k + "\":\"" + v + "\"");
    }

    private static String kv(String k, int v) {
        return String.format("\"" + k + "\":" + v);
    }

    private static String ambientOcclusion(boolean enabled) {
        return "\"ambientocclusion\":" + enabled + ",";
    }

    private static String variant(TemplateProvider t, String state, String model) throws IOException {
        return t.load("blockstate/var.json", d -> d
                .def("state", state)
                .def("model", model)
                .def("extra", ""));
    }

    private static String variantY(TemplateProvider t, String state, String model, int y) throws IOException {
        return t.load("blockstate/var.json", d -> d
                .def("state", state)
                .def("model", model)
                .def("extra", kv("y", y) + ","));
    }

    private static String variantXY(TemplateProvider t, String state, String model, int x, int y) throws IOException {
        return t.load("blockstate/var.json", d -> d
                .def("state", state)
                .def("model", model)
                .def("extra", list(kv("x", x), kv("y", y)) + ","));
    }

    private static String variantRotation16(TemplateProvider t, String keyPrefix, String modelPrefix) throws IOException {
        return list(
                variantY(t, keyPrefix + "0", modelPrefix + "_0", 180),
                variantY(t, keyPrefix + "1", modelPrefix + "_67_5", 270),
                variantY(t, keyPrefix + "2", modelPrefix + "_45", 270),
                variantY(t, keyPrefix + "3", modelPrefix + "_22_5", 270),
                variantY(t, keyPrefix + "4", modelPrefix + "_0", 270),
                variant(t, keyPrefix + "5", modelPrefix + "_67_5"),
                variant(t, keyPrefix + "6", modelPrefix + "_45"),
                variant(t, keyPrefix + "7", modelPrefix + "_22_5"),
                variant(t, keyPrefix + "8", modelPrefix + "_0"),
                variantY(t, keyPrefix + "9", modelPrefix + "_67_5", 90),
                variantY(t, keyPrefix + "10", modelPrefix + "_45", 90),
                variantY(t, keyPrefix + "11", modelPrefix + "_22_5", 90),
                variantY(t, keyPrefix + "12", modelPrefix + "_0", 90),
                variantY(t, keyPrefix + "13", modelPrefix + "_67_5", 180),
                variantY(t, keyPrefix + "14", modelPrefix + "_45", 180),
                variantY(t, keyPrefix + "15", modelPrefix + "_22_5", 180)
        );
    }

    private static String variantHFacing(TemplateProvider t, String keyPrefix, String model) throws IOException {
        return list(
                variant(t, keyPrefix + "north", model),
                variantY(t, keyPrefix + "west", model, 270),
                variantY(t, keyPrefix + "south", model, 180),
                variantY(t, keyPrefix + "east", model, 90)
        );
    }

    private static void addChestLikeModel(String parent, String chestTex, String chestName, Identifier modelId, EBEPack pack) {
        pack.addTemplateResource(id(modelId.getNamespace(), "models/" + modelId.getPath() + ".json"),
                t -> t.load("model/chest_like.json", d -> d
                        .def("parent", parent)
                        .def("chest_tex", spriteAlias("entity/chest/" + chestTex))
                        .def("ao", ambientOcclusion(EnhancedBlockEntities.CONFIG.chestAO))
                        .def("particle", chestParticle(chestName))));
    }

    public static void addSingleChestModels(String chestTex, String chestName, EBEPack pack) {
        addChestLikeModel("template_chest_center", chestTex, chestName, vanillaId("block/" + chestName + "_center"), pack);
        addChestLikeModel("template_chest_center_lid", chestTex, chestName, vanillaId("block/" + chestName + "_center_lid"), pack);
        addChestLikeModel("template_chest_center_trunk", chestTex, chestName, vanillaId("block/" + chestName + "_center_trunk"), pack);
    }

    public static void addDoubleChestModels(String leftTex, String rightTex, String chestName, EBEPack pack) {
        addChestLikeModel("template_chest_left", leftTex, chestName, vanillaId("block/" + chestName + "_left"), pack);
        addChestLikeModel("template_chest_left_lid", leftTex, chestName, vanillaId("block/" + chestName + "_left_lid"), pack);
        addChestLikeModel("template_chest_left_trunk", leftTex, chestName, vanillaId("block/" + chestName + "_left_trunk"), pack);
        addChestLikeModel("template_chest_right", rightTex, chestName, vanillaId("block/" + chestName + "_right"), pack);
        addChestLikeModel("template_chest_right_lid", rightTex, chestName, vanillaId("block/" + chestName + "_right_lid"), pack);
        addChestLikeModel("template_chest_right_trunk", rightTex, chestName, vanillaId("block/" + chestName + "_right_trunk"), pack);
    }

    private static String chestParticle(String chestName) {
        if (EnhancedBlockEntities.CONFIG.experimentalChests) {
            return kv("particle", "block/" + chestName + "_particle") + ",";
        }
        return "";
    }

    private static String bedParticle(String bedColor) {
        if (EnhancedBlockEntities.CONFIG.experimentalBeds) {
            return kv("particle", "block/" + bedColor + "_bed_particle") + ",";
        }
        return "";
    }

    private static String signParticle(String signName) {
        if (EnhancedBlockEntities.CONFIG.experimentalSigns) {
            return kv("particle", "block/" + signName + "_particle") + ",";
        }
        return "";
    }

    private static void addBlockState(Identifier blockId, TemplateProvider.TemplateApplyingFunction vars, EBEPack pack) {
        pack.addTemplateResource(id(blockId.getNamespace(), "blockstates/" + blockId.getPath() + ".json"),
                t -> t.load("blockstate/base.json", d -> d.def("vars", vars)));
    }

    public static void addChestBlockStates(String chestName, EBEPack pack) {
        addBlockState(vanillaId(chestName),
                t -> list(
                        variantHFacing(t, "type=single,facing=", "block/" + chestName + "_center"),
                        variantHFacing(t, "type=left,facing=", "block/" + chestName + "_left"),
                        variantHFacing(t, "type=right,facing=", "block/" + chestName + "_right")
                ), pack);
    }

    public static void addSingleChestOnlyBlockStates(String chestName, EBEPack pack) {
        addBlockState(vanillaId(chestName),
                t -> list(variantHFacing(t, "facing=", "block/" + chestName + "_center")), pack);
    }

    public static void addParentModel(String parent, Identifier modelId, EBEPack pack) {
        pack.addTemplateResource(id(modelId.getNamespace(), "models/" + modelId.getPath() + ".json"), t ->
                "{" + kv("parent", parent) + "}");
    }

    public static void addParentTexModel(String parent, String textures, Identifier modelId, EBEPack pack) {
        addParentTexModel(parent, textures, modelId, pack, "");
    }

    public static void addParentTexModel(String parent, String textures, Identifier modelId, EBEPack pack, String extra) {
        pack.addTemplateResource(id(modelId.getNamespace(), "models/" + modelId.getPath() + ".json"), t ->
                t.load("model/parent_and_tex.json", d -> d.def("parent", parent).def("textures", textures).def("ao", extra)));
    }

    public static void addSignTypeModels(String signType, EBEPack pack) {
        var signName = signType + "_sign";
        var signTex = spriteAlias("block/" + signName);
        addRotation16Models(
                signParticle(signName) + kv("sign", signTex),
                "block/template_sign", "block/" + signName, ResourceUtil::signAOSuffix, pack);

        var hangingTexDef = list(
                kv("sign", spriteAlias("block/" + signType + "_hanging_sign")),
                kv("particle", spriteAlias("block/" + signType + "_hanging_sign"))
        );
        addRotation16Models(hangingTexDef, "block/template_hanging_sign", "block/" + signType + "_hanging_sign",
                ResourceUtil::signAOSuffix, pack);
        addRotation16Models(hangingTexDef, "block/template_hanging_sign_attached", "block/" + signType + "_hanging_sign_attached",
                ResourceUtil::signAOSuffix, pack);

        addParentTexModel(signAOSuffix("block/template_wall_sign"),
                signParticle(signName) + kv("sign", signTex), vanillaId("block/" + signType + "_wall_sign"), pack);
        addParentTexModel(signAOSuffix("block/template_wall_hanging_sign"),
                hangingTexDef, vanillaId("block/" + signType + "_wall_hanging_sign"), pack);
    }

    public static void addRotation16Models(String textures, String templatePrefix, String modelPrefix, Function<String, String> suffix, EBEPack pack) {
        addParentTexModel(suffix.apply(templatePrefix + "_0"), textures, vanillaId(modelPrefix + "_0"), pack);
        addParentTexModel(suffix.apply(templatePrefix + "_22_5"), textures, vanillaId(modelPrefix + "_22_5"), pack);
        addParentTexModel(suffix.apply(templatePrefix + "_45"), textures, vanillaId(modelPrefix + "_45"), pack);
        addParentTexModel(suffix.apply(templatePrefix + "_67_5"), textures, vanillaId(modelPrefix + "_67_5"), pack);
    }

    private static String signAOSuffix(String model) {
        if (EnhancedBlockEntities.CONFIG.signAO) {
            model += "_ao";
        }
        return model;
    }

    public static void addSignBlockStates(String signName, String wallSignName, EBEPack pack) {
        addBlockState(vanillaId(signName), t -> variantRotation16(t, "rotation=", "block/" + signName), pack);
        addBlockState(vanillaId(wallSignName), t -> variantHFacing(t, "facing=", "block/" + wallSignName), pack);
    }

    public static void addHangingSignBlockStates(String signName, String wallSignName, EBEPack pack) {
        addBlockState(vanillaId(signName),
                t -> list(
                        variantRotation16(t, "attached=false,rotation=", "block/" + signName),
                        variantRotation16(t, "attached=true,rotation=", "block/" + signName + "_attached")
                ), pack);

        addBlockState(vanillaId(wallSignName), t -> variantHFacing(t, "facing=", "block/" + wallSignName), pack);
    }

    public static void addBellBlockState(EBEPack pack) {
        addBlockState(vanillaId("bell"),
                t -> {
                    var vars = new DelimitedAppender(",");
                    for (Direction dir : EBEUtil.HORIZONTAL_DIRECTIONS) {
                        int rot = EBEUtil.angle(dir) + 90;
                        vars
                                .append(variantY(t, "attachment=double_wall,facing=" + dir.getName(), "block/bell_between_walls_with_bell", rot))
                                .append(variantY(t, "attachment=ceiling,facing=" + dir.getName(), "block/bell_ceiling_with_bell", rot + 90))
                                .append(variantY(t, "attachment=floor,facing=" + dir.getName(), "block/bell_floor_with_bell", rot + 90))
                                .append(variantY(t, "attachment=single_wall,facing=" + dir.getName(), "block/bell_wall_with_bell", rot));
                    }
                    return vars.get();
                }, pack);
    }

    public static void addBedModels(DyeColor bedColor, EBEPack pack) {
        String color = bedColor.getName();

        addParentTexModel(bedAOSuffix("block/template_bed_head"),
                bedParticle(color) + kv("bed", spriteAlias("entity/bed/" + color)),
                vanillaId("block/" + color + "_bed_head"), pack);
        addParentTexModel(bedAOSuffix("block/template_bed_foot"),
                bedParticle(color) + kv("bed", spriteAlias("entity/bed/" + color)),
                vanillaId("block/" + color + "_bed_foot"), pack);

        addBedItemDefinition(color, pack);
    }

    public static void addBedBlockState(DyeColor bedColor, EBEPack pack) {
        String color = bedColor.getName();
        addBlockState(vanillaId(color + "_bed"),
                t -> {
                    var vars = new DelimitedAppender(",");
                    for (Direction dir : EBEUtil.HORIZONTAL_DIRECTIONS) {
                        int rot = EBEUtil.angle(dir) + 180;
                        vars
                                .append(variantY(t, "part=head,facing=" + dir.getName(), "block/" + bedColor + "_bed_head", rot))
                                .append(variantY(t, "part=foot,facing=" + dir.getName(), "block/" + bedColor + "_bed_foot", rot));
                    }
                    return vars.get();
                }, pack);
    }

    private static String bedAOSuffix(String model) {
        if (EnhancedBlockEntities.CONFIG.bedAO) {
            model += "_ao";
        }
        return model;
    }

    public static void addShulkerBoxModels(@Nullable DyeColor color, EBEPack pack) {
        var texture = color != null
                ? spriteAlias("entity/shulker/shulker_" + color.getName())
                : spriteAlias("entity/shulker/shulker");
        var shulkerBoxStr = color != null ? color.getName() + "_shulker_box" : "shulker_box";
        var particle = "block/" + shulkerBoxStr;
        var ao = ambientOcclusion(EnhancedBlockEntities.CONFIG.shulkerBoxAO);
        addParentTexModel("block/template_shulker_box",
                list(kv("shulker", texture), kv("particle", particle)),
                vanillaId("block/" + shulkerBoxStr), pack, ao);
        addParentTexModel("block/template_shulker_box_bottom",
                list(kv("shulker", texture), kv("particle", particle)),
                vanillaId("block/" + shulkerBoxStr + "_bottom"), pack, ao);
        addParentTexModel("block/template_shulker_box_lid",
                list(kv("shulker", texture), kv("particle", particle)),
                vanillaId("block/" + shulkerBoxStr + "_lid"), pack, ao);
    }

    public static void addShulkerBoxBlockStates(@Nullable DyeColor color, EBEPack pack) {
        var shulkerBoxStr = color != null ? color.getName() + "_shulker_box" : "shulker_box";
        addBlockState(vanillaId(shulkerBoxStr),
                t -> {
                    var vars = new DelimitedAppender(",");
                    vars
                            .append(variant(t, "facing=up", "block/" + shulkerBoxStr))
                            .append(variantXY(t, "facing=down", "block/" + shulkerBoxStr, 180, 0));
                    for (Direction dir : EBEUtil.HORIZONTAL_DIRECTIONS) {
                        int rot = EBEUtil.angle(dir) + 180;
                        vars.append(variantXY(t, "facing=" + dir.getName(), "block/" + shulkerBoxStr, 90, rot));
                    }
                    return vars.get();
                }, pack);
    }

    public static void addDecoratedPotBlockState(EBEPack pack) {
        addBlockState(vanillaId("decorated_pot"),
                t -> variantHFacing(t, "facing=", "block/decorated_pot_base"), pack);
    }

    public static void addDecoratedPotPatternModels(ResourceKey<DecoratedPotPattern> patternKey, EBEPack pack) {
        @SuppressWarnings("null")
        var patternSprite = decoratedPotPatternSprite(patternKey);
        addAliasedBlockSprite(patternSprite, pack);

        for (Direction dir : EBEUtil.HORIZONTAL_DIRECTIONS) {
            addParentTexModel("block/template_pottery_pattern_" + dir.getName(),
                    list(kv("pattern", spriteAlias(patternSprite).toString()),
                            kv("particle", "block/terracotta")),
                    vanillaId("block/" + patternKey.identifier().getPath() + "_" + dir.getName()),
                    pack);
        }
    }

    private static Identifier decoratedPotPatternSprite(ResourceKey<DecoratedPotPattern> patternKey) {
        var id = patternKey.identifier();
        var path = id.getPath();
        if ("blank".equals(path)) {
            return Identifier.withDefaultNamespace("entity/decorated_pot/decorated_pot_side");
        }

        return Identifier.fromNamespaceAndPath(id.getNamespace(), "entity/decorated_pot/" + path + "_pottery_pattern");
    }

    public static void resetBasePack() {
        BASE_PACK = new EBEPack(EBEUtil.id("base_resources"), EnhancedBlockEntities.TEMPLATE_LOADER);
    }

    public static void resetTopLevelPack() {
        TOP_LEVEL_PACK = new EBEPack(EBEUtil.id("top_level_resources"), EnhancedBlockEntities.TEMPLATE_LOADER);
    }

    public static EBEPack getBasePack() {
        return BASE_PACK;
    }

    public static EBEPack getTopLevelPack() {
        return TOP_LEVEL_PACK;
    }

    public static EBEPack getPackForCompat() {
        if (EnhancedBlockEntities.CONFIG.forceResourcePackCompat) {
            return getTopLevelPack();
        }

        return getBasePack();
    }

    public static void dumpModAssets(Path dest) throws IOException {
        var roots = FabricLoader.getInstance().getModContainer(EnhancedBlockEntities.ID)
                .map(ModContainer::getRootPaths).orElse(List.of());

        for (var root : roots) {
            try (var sourceAssets = Files.walk(root.resolve("assets"))) {
                for (var asset : sourceAssets.collect(Collectors.toSet())) {
                    if (!Files.isDirectory(asset)) {
                        var out = dest.resolve(root.relativize(asset));
                        if (!Files.exists(out.getParent())) {
                            Files.createDirectories(out.getParent());
                        }
                        Files.copy(asset, out, Files.exists(out) ? new CopyOption[] {StandardCopyOption.REPLACE_EXISTING} : new CopyOption[] {});
                    }
                }
            }
        }
    }

    public static void dumpAllPacks(Path dest) throws IOException {
        getBasePack().dump(dest);
        getTopLevelPack().dump(dest);
        dumpModAssets(dest);
    }

    private static class DelimitedAppender {
        private final StringBuilder builder = new StringBuilder();
        private final CharSequence delimiter;

        private DelimitedAppender(CharSequence delimiter) {
            this.delimiter = delimiter;
        }

        public DelimitedAppender append(CharSequence seq) {
            if (!this.builder.isEmpty()) {
                this.builder.append(this.delimiter);
            }

            this.builder.append(seq);
            return this;
        }

        public String get() {
            return this.builder.toString();
        }
    }

    static {
        resetBasePack();
        resetTopLevelPack();
    }
}
