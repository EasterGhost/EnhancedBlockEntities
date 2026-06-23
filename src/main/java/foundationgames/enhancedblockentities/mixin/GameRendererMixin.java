package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.client.gui.EBESignBlockRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.resources.model.ModelManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.ArrayList;
import java.util.List;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyArgs(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/render/GuiRenderer;<init>(Lnet/minecraft/client/renderer/state/gui/GuiRenderState;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher;Ljava/util/List;)V"
            )
    )
    private void enhanced_bes$addSignBlockPictureInPictureRenderer(Args args, Minecraft minecraft,
            ItemInHandRenderer itemInHandRenderer, RenderBuffers renderBuffers, ModelManager modelManager) {
        List<PictureInPictureRenderer<?>> renderers = new ArrayList<>(args.get(4));
        renderers.add(new EBESignBlockRenderer(renderBuffers.bufferSource()));
        args.set(4, renderers);
    }
}
