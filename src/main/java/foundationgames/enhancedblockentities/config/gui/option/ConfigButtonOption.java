package foundationgames.enhancedblockentities.config.gui.option;

import com.mojang.serialization.Codec;
import foundationgames.enhancedblockentities.config.gui.screen.EBEConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConfigButtonOption {
    @SuppressWarnings("null")
    public static OptionInstance<?> getOption(Screen parent) {
        return new OptionInstance<>(
                "option.ebe.config",
                OptionInstance.noTooltip(),
                (title, value) -> title,
                new ConfigButtonValueSet(parent),
                Codec.BOOL,
                false,
                value -> {
                }
        );
    }

    private record ConfigButtonValueSet(Screen parent) implements OptionInstance.ValueSet<Boolean> {
        @SuppressWarnings("null")
        @Override
        public Function<OptionInstance<Boolean>, AbstractWidget> createButton(
                OptionInstance.TooltipSupplier<Boolean> tooltip,
                Options options,
                int x,
                int y,
                int width,
                Consumer<Boolean> changed
        ) {
            return option -> Button.builder(Component.translatable("option.ebe.config"), button ->
                    Minecraft.getInstance().setScreen(new EBEConfigScreen(parent))
            ).bounds(x, y, width, 20).build();
        }

        @SuppressWarnings("null")
        @Override
        public Optional<Boolean> validateValue(Boolean value) {
            return Optional.of(value);
        }

        @SuppressWarnings("null")
        @Override
        public Codec<Boolean> codec() {
            return Codec.BOOL;
        }
    }
}
