package foundationgames.enhancedblockentities.config.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class SectionTextWidget extends AbstractWidget {
    private final Font font;

    public SectionTextWidget(Component message, Font font) {
        this(0, 0, 200, 20, message, font);
    }

    public SectionTextWidget(int x, int y, int width, int height, Component message, Font font) {
        super(x, y, width, height, message);
        this.font = font;
        this.active = false;
    }

    @SuppressWarnings("null")
    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float tickDelta) {
        int y = (this.getY() + this.getHeight()) - 6;
        int left = this.getX() + 1;
        int right = this.getX() + this.getWidth() - 1;
        int textWidth = this.font.width(this.getMessage());
        int textLeft = this.getX() + ((this.getWidth() - textWidth) / 2) - 5;
        int textRight = textLeft + textWidth + 10;
        int textX = this.getX() + (this.getWidth() / 2);
        int textY = y - (this.font.lineHeight / 2);

        graphics.fill(left, y, textLeft, y + 2, 0xFFFFFFFF);
        graphics.fill(textRight, y, right, y + 2, 0xFFFFFFFF);
        graphics.centeredText(this.font, this.getMessage(), textX, textY, 0xFFFFFF);
    }

    @SuppressWarnings("null")
    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, this.getMessage());
    }
}
