package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.util.EBEUtil;
import foundationgames.enhancedblockentities.util.ResourceUtil;
import foundationgames.enhancedblockentities.util.hacks.ExperimentalSetup;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(MultiPackResourceManager.class)
public abstract class MultiPackResourceManagerMixin {
    @Shadow @Final private Map<String, FallbackResourceManager> namespacedManagers;

    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static List<PackResources> enhanced_bes$injectBasePack(List<PackResources> old) {
        var packs = new ArrayList<>(old);

        int idx = 0;
        if (!packs.isEmpty()) {
            do {
                idx++;
            } while (idx < packs.size() && !EBEUtil.isVanillaResourcePack(packs.get(idx - 1)));
        }
        packs.add(idx, ResourceUtil.getBasePack());

        return packs;
    }

    @SuppressWarnings("null")
    @Inject(method = "<init>", at = @At("TAIL"))
    private void enhanced_bes$injectTopLevelPack(PackType type, List<PackResources> packs, CallbackInfo ci) {
        if (type != PackType.CLIENT_RESOURCES) {
            return;
        }

        ExperimentalSetup.cacheResources((ResourceManager) this);
        ExperimentalSetup.setup();

        var pack = ResourceUtil.getTopLevelPack();
        for (var namespace : pack.getNamespaces(type)) {
            this.namespacedManagers.computeIfAbsent(namespace, n -> new FallbackResourceManager(type, n)).push(pack);
        }
    }
}
