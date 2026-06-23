package foundationgames.enhancedblockentities.config.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WidgetRowListWidget extends ContainerObjectSelectionList<WidgetRowListWidget.Entry> {
    public static final int SPACING = 4;

    private final int rowWidth;
    private final int rowHeight;

    public WidgetRowListWidget(Minecraft minecraft, int width, int height, int y, int rowWidth, int rowHeight) {
        super(minecraft, width, height, y, rowHeight + SPACING);
        this.rowWidth = rowWidth;
        this.rowHeight = rowHeight;
    }

    public void add(AbstractWidget... widgets) {
        if (widgets.length == 0) {
            return;
        }

        int widgetWidth = (this.rowWidth - ((widgets.length - 1) * SPACING)) / widgets.length;
        for (var widget : widgets) {
            widget.setSize(widgetWidth, this.rowHeight);
        }

        this.addEntry(new Entry(this.rowWidth, this.rowHeight, Arrays.asList(widgets)));
    }

    @Override
    public int getRowWidth() {
        return this.rowWidth;
    }

    @Override
    protected int scrollBarX() {
        return this.getX() + this.getWidth() - 6;
    }

    public static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        private final int rowWidth;
        private final int rowHeight;
        private final List<AbstractWidget> children;

        public Entry(int rowWidth, int rowHeight, List<AbstractWidget> widgets) {
            this.rowWidth = rowWidth;
            this.rowHeight = rowHeight;
            this.children = new ArrayList<>(widgets);
        }

        @SuppressWarnings("null")
        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        @SuppressWarnings("null")
        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.children;
        }

        @SuppressWarnings("null")
        @Override
        public void visitWidgets(java.util.function.Consumer<AbstractWidget> consumer) {
            this.layoutChildren();
            this.children.forEach(consumer);
        }

        @SuppressWarnings("null")
        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.layoutChildren();
            for (var widget : this.children) {
                widget.extractRenderState(graphics, mouseX, mouseY, tickDelta);
            }
        }

        private void layoutChildren() {
            int count = this.children.size();
            int widgetWidth = (this.rowWidth - ((count - 1) * SPACING)) / count;
            int x = this.getContentX();
            int y = this.getY();

            for (var widget : this.children) {
                widget.setX(x);
                widget.setY(y);
                widget.setSize(widgetWidth, this.rowHeight);
                x += widgetWidth + SPACING;
            }
        }
    }
}
