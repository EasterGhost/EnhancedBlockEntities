package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.util.duck.EBEShulkerBoxRenderState;
import net.minecraft.client.renderer.blockentity.state.ShulkerBoxRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ShulkerBoxRenderState.class)
public class ShulkerBoxRenderStateMixin implements EBEShulkerBoxRenderState {
    @Unique
    private boolean enhanced_bes$renderOnlyLid;

    @Override
    public boolean enhanced_bes$renderOnlyLid() {
        return this.enhanced_bes$renderOnlyLid;
    }

    @Override
    public void enhanced_bes$renderOnlyLid(boolean renderOnlyLid) {
        this.enhanced_bes$renderOnlyLid = renderOnlyLid;
    }
}
