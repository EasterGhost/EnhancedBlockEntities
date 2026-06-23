package foundationgames.enhancedblockentities.config.gui.option;

import foundationgames.enhancedblockentities.ReloadType;
import foundationgames.enhancedblockentities.config.EBEConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class EBEOption {
    private static final Component NEWLINE = Component.literal("\n");
    private static final String DIVIDER = "text.ebe.option_value_division";
    private static final String OVERRIDDEN = "warning.ebe.overridden";

    public final String key;
    public final boolean hasValueComments;
    public final ReloadType reloadType;
    public final TextPalette palette;
    public final @Nullable EBEConfig.Override override;

    private final List<String> values;
    private final int defaultValue;
    private int selected;
    private Tooltip tooltip = null;
    private Component text = null;

    public EBEOption(String key, List<String> values, ConfigView config, boolean hasValueComments, TextPalette palette, ReloadType reloadType) {
        this.key = key;
        this.values = values;
        this.defaultValue = Mth.clamp(values.indexOf(config.configValues.getProperty(key)), 0, values.size() - 1);
        this.override = config.overrides.get(key);
        this.selected = this.defaultValue;
        this.hasValueComments = hasValueComments;
        this.palette = palette;
        this.reloadType = reloadType;
    }

    public String getValue() {
        return values.get(selected);
    }

    @SuppressWarnings("null")
    public Component getText() {
        if (this.text == null) {
            var option = Component.translatable(this.getOptionKey()).withStyle(style -> style.withColor(isDefault() ? 0xFFFFFF : 0xFFDA5E));
            var value = Component.translatable(this.getValueKey()).withStyle(style -> style.withColor(this.palette.getColor((float) this.selected / this.values.size())));
            this.text = option.append(Component.translatable(DIVIDER).append(value));
        }

        return this.text;
    }

    @SuppressWarnings("null")
    public Tooltip getTooltip() {
        if (this.tooltip == null) {
            if (override != null) {
                var text = Component.translatable(OVERRIDDEN, override.modResponsible().getMetadata().getId())
                        .withStyle(ChatFormatting.RED, ChatFormatting.UNDERLINE);
                if (override.reason() != null) {
                    text.append(NEWLINE).append(override.reason());
                }
                this.tooltip = Tooltip.create(text);
            } else if (hasValueComments) {
                this.tooltip = Tooltip.create(Component.translatable(String.format("option.ebe.%s.valueComment.%s", key, getValue()))
                        .append(NEWLINE)
                        .append(Component.translatable(String.format("option.ebe.%s.comment", key))));
            } else {
                this.tooltip = Tooltip.create(Component.translatable(String.format("option.ebe.%s.comment", key)));
            }
        }

        return this.tooltip;
    }

    public void next() {
        selected++;
        if (selected >= values.size()) {
            selected = 0;
        }

        tooltip = null;
        text = null;
    }

    public boolean isDefault() {
        return selected == defaultValue;
    }

    private String getOptionKey() {
        return String.format("option.ebe.%s", key);
    }

    private String getValueKey() {
        return String.format("value.ebe.%s", getValue());
    }

    public record ConfigView(Properties configValues, Map<String, EBEConfig.Override> overrides) {}
}
