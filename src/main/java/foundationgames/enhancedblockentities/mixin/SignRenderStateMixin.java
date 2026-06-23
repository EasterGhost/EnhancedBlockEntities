package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.util.duck.EBESignRenderState;
import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SignRenderState.class)
public class SignRenderStateMixin implements EBESignRenderState {
    @Unique private boolean enhanced_bes$skipSignBody;

    @Override
    public void enhanced_bes$setSkipSignBody(boolean skipSignBody) {
        this.enhanced_bes$skipSignBody = skipSignBody;
    }

    @Override
    public boolean enhanced_bes$skipSignBody() {
        return this.enhanced_bes$skipSignBody;
    }
}
