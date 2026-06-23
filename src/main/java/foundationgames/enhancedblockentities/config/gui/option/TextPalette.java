package foundationgames.enhancedblockentities.config.gui.option;

public interface TextPalette {
    TextPalette ON_OFF = progress -> progress < 0.5f ? 0x55FF55 : 0xFF5555;

    int getColor(float progress);

    static TextPalette rainbow(float saturation) {
        return progress -> java.awt.Color.HSBtoRGB(progress, saturation, 1.0f);
    }
}
