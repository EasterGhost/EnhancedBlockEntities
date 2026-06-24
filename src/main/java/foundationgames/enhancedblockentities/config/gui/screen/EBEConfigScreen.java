package foundationgames.enhancedblockentities.config.gui.screen;

import com.google.common.collect.ImmutableList;
import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.ReloadType;
import foundationgames.enhancedblockentities.config.EBEConfig;
import foundationgames.enhancedblockentities.config.gui.option.EBEOption;
import foundationgames.enhancedblockentities.config.gui.option.TextPalette;
import foundationgames.enhancedblockentities.config.gui.widget.SectionTextWidget;
import foundationgames.enhancedblockentities.config.gui.widget.WidgetRowListWidget;
import foundationgames.enhancedblockentities.util.EBEUtil;
import foundationgames.enhancedblockentities.util.GuiUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class EBEConfigScreen extends Screen {
    private static final ImmutableList<String> BOOLEAN_OPTIONS = ImmutableList.of("true", "false");
    private static final ImmutableList<String> ALLOWED_FORCED_DISABLED = ImmutableList.of("allowed", "forced", "disabled");
    private static final ImmutableList<String> SIGN_TEXT_OPTIONS = ImmutableList.of("smart", "all", "most", "some", "few");
    private static final Component HOLD_DESCRIPTIONS = Component.translatable("text.ebe.descriptions")
            .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);

    private final List<EBEOption> options = new ArrayList<>();
    private final Screen parent;
    private WidgetRowListWidget optionList;

    public EBEConfigScreen(Screen parent) {
        super(Component.translatable("screen.ebe.config"));
        this.parent = parent;
    }

    @SuppressWarnings("null")
    @Override
    protected void init() {
        super.init();
        this.options.clear();

        var config = new Properties();
        EnhancedBlockEntities.CONFIG.writeTo(config);
        var configView = new EBEOption.ConfigView(config, EnhancedBlockEntities.CONFIG.overrides);

        int buttonWidth = 180;
        int buttonHeight = 20;
        int spacing = 4;
        int rowWidth = (buttonWidth * 2) + spacing;
        int y = 34;
        int bottomY = this.height - 27;
        int listHeight = Math.max(40, bottomY - y);
        this.optionList = new WidgetRowListWidget(this.minecraft, this.width, listHeight, y, rowWidth, buttonHeight);
        this.addRenderableWidget(this.optionList);

        this.addSection("text.ebe.chest_options");
        this.addOptionRow(
                new EBEOption(EBEConfig.RENDER_ENHANCED_CHESTS_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES),
                new EBEOption(EBEConfig.CHEST_AO_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES));
        this.addOptionRow(
                new EBEOption(EBEConfig.EXPERIMENTAL_CHESTS_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES),
                new EBEOption(EBEConfig.CHRISTMAS_CHESTS_KEY, ALLOWED_FORCED_DISABLED, configView, true, TextPalette.rainbow(0.35f), ReloadType.WORLD));

        this.addSection("text.ebe.sign_options");
        this.addOptionRow(
                new EBEOption(EBEConfig.RENDER_ENHANCED_SIGNS_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES),
                new EBEOption(EBEConfig.SIGN_TEXT_RENDERING_KEY, SIGN_TEXT_OPTIONS, configView, true, TextPalette.rainbow(0.45f), ReloadType.NONE));
        this.addOptionRow(
                new EBEOption(EBEConfig.EXPERIMENTAL_SIGNS_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES),
                new EBEOption(EBEConfig.SIGN_AO_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES));

        this.addSection("text.ebe.bell_options");
        this.addOptionRow(
                new EBEOption(EBEConfig.RENDER_ENHANCED_BELLS_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES),
                new EBEOption(EBEConfig.BELL_AO_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES));

        this.addSection("text.ebe.bed_options");
        this.addOptionRow(
                new EBEOption(EBEConfig.RENDER_ENHANCED_BEDS_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES),
                new EBEOption(EBEConfig.EXPERIMENTAL_BEDS_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES));
        this.addOptionRow(new EBEOption(EBEConfig.BED_AO_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES));

        this.addSection("text.ebe.shulker_box_options");
        this.addOptionRow(
                new EBEOption(EBEConfig.RENDER_ENHANCED_SHULKER_BOXES_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES),
                new EBEOption(EBEConfig.SHULKER_BOX_AO_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES));

        this.addSection("text.ebe.decorated_pot_options");
        this.addOptionRow(
                new EBEOption(EBEConfig.RENDER_ENHANCED_DECORATED_POTS_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES),
                new EBEOption(EBEConfig.DECORATED_POT_AO_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES));

        this.addSection("text.ebe.advanced");
        this.addOptionRow(new EBEOption(EBEConfig.FORCE_RESOURCE_PACK_COMPAT_KEY, BOOLEAN_OPTIONS, configView, false, TextPalette.ON_OFF, ReloadType.RESOURCES));
        this.optionList.add(Button.builder(Component.translatable("option.ebe.dump"), button -> {
            try {
                EBEUtil.dumpResources();
            } catch (IOException e) {
                EnhancedBlockEntities.LOG.error("Error dumping EBE resources", e);
            }
        }).tooltip(Tooltip.create(GuiUtil.shorten(I18n.get("option.ebe.dump.comment"), 20))).build());

        int center = this.width / 2;
        this.addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> this.onClose())
                .bounds(center - 154, bottomY, 100, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("text.ebe.apply"), button -> this.applyChanges())
                .bounds(center - 50, bottomY, 100, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> {
            this.applyChanges();
            this.onClose();
        }).bounds(center + 54, bottomY, 100, 20).build());
    }

    @SuppressWarnings("null")
    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float tickDelta) {
        super.extractBackground(graphics, mouseX, mouseY, tickDelta);
        this.extractMenuBackground(graphics, 0, 0, this.width, 34);
        this.extractMenuBackground(graphics, 0, this.height - 35, this.width, this.height);
    }

    @SuppressWarnings("null")
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float tickDelta) {
        super.extractRenderState(graphics, mouseX, mouseY, tickDelta);
        graphics.centeredText(this.font, this.title, this.width / 2, 8, 0xFFFFFF);
        graphics.centeredText(this.font, HOLD_DESCRIPTIONS, this.width / 2, 21, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreenAndShow(this.parent);
    }

    public void applyChanges() {
        EBEConfig config = EnhancedBlockEntities.CONFIG;
        Properties properties = new Properties();
        AtomicReference<ReloadType> type = new AtomicReference<>(ReloadType.NONE);

        options.forEach(option -> {
            if (!option.isDefault()) {
                type.set(type.get().or(option.reloadType));
            }
            properties.setProperty(option.key, option.getValue());
        });

        config.readFrom(properties);
        config.save();
        EnhancedBlockEntities.reload(type.get());
    }

    @SuppressWarnings("null")
    private void addSection(String translationKey) {
        this.optionList.add(new SectionTextWidget(Component.translatable(translationKey), this.font));
    }

    private void addOptionRow(EBEOption... rowOptions) {
        Button[] buttons = new Button[rowOptions.length];
        for (int i = 0; i < rowOptions.length; i++) {
            buttons[i] = this.createOptionButton(rowOptions[i]);
        }

        this.optionList.add(buttons);
    }

    @SuppressWarnings("null")
    private Button createOptionButton(EBEOption option) {
        this.options.add(option);

        var button = Button.builder(option.getText(), b -> {
            option.next();
            b.setMessage(option.getText());
            b.setTooltip(option.getTooltip());
        }).tooltip(option.getTooltip()).build();

        if (option.override != null) {
            button.active = false;
        }

        return button;
    }
}
