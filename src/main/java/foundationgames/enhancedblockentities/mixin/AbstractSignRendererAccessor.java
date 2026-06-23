package foundationgames.enhancedblockentities.mixin;

import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractSignRenderer.class)
public interface AbstractSignRendererAccessor {
    @Accessor("OUTLINE_RENDER_DISTANCE")
    static int enhanced_bes$getOutlineRenderDistance() {
        throw new AssertionError();
    }
}
