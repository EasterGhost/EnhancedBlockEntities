package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.util.duck.EBEChestRenderState;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChestRenderState.class)
public class ChestRenderStateMixin implements EBEChestRenderState {
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
