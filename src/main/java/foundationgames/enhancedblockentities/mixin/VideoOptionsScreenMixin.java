package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.config.gui.option.ConfigButtonOption;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoSettingsScreen.class)
public abstract class VideoOptionsScreenMixin extends OptionsSubScreen {
    protected VideoOptionsScreenMixin(Screen lastScreen, Options options, Component title) {
        super(lastScreen, options, title);
    }

    @SuppressWarnings("null")
    @Inject(method = "addOptions", at = @At("TAIL"))
    private void enhanced_bes$addEBEOptionButton(CallbackInfo ci) {
        this.list.addBig(ConfigButtonOption.getOption((Screen)(Object)this));
    }
}
