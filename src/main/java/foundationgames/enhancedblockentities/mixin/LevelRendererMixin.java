package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.util.WorldUtil;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SectionRenderDispatcher.RenderSection.class)
public class LevelRendererMixin {
    @Inject(method = "setSectionMesh", at = @At("RETURN"))
    private void enhanced_bes$runPostRebuildTasks(SectionMesh mesh, CallbackInfoReturnable<SectionMesh> cir) {
        if (mesh != CompiledSectionMesh.UNCOMPILED) {
            var section = (SectionRenderDispatcher.RenderSection) (Object) this;
            WorldUtil.runChunkUpdateTasks(section.getSectionNode());
        }
    }
}
