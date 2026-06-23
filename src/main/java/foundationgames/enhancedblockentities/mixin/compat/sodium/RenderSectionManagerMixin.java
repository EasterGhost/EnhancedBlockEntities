package foundationgames.enhancedblockentities.mixin.compat.sodium;

import foundationgames.enhancedblockentities.util.WorldUtil;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSection;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.BuilderTaskOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Pseudo
@Mixin(value = RenderSectionManager.class, remap = false)
public class RenderSectionManagerMixin {
    @Inject(method = "processChunkBuildResults", at = @At("RETURN"), require = 0)
    private void enhanced_bes$runPostRebuildTasks(ArrayList<BuilderTaskOutput> outputs, CallbackInfoReturnable<Boolean> cir) {
        for (var output : outputs) {
            RenderSection section = output.render;
            if (!section.isDisposed() && section.getLastUploadFrame() == output.submitTime) {
                WorldUtil.runChunkUpdateTasks(section.getPosition().asLong());
            }
        }
    }
}
