package foundationgames.enhancedblockentities.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;

public final class SignRenderManager {
    private static int lastRenderedSigns = 0;
    private static int renderedSigns = 0;

    private SignRenderManager() {}

    public static int getRenderedSignAmount() {
        return lastRenderedSigns;
    }

    public static void countRenderedSign() {
        renderedSigns++;
    }

    public static void endFrame(LevelRenderContext context) {
        lastRenderedSigns = renderedSigns;
        renderedSigns = 0;
    }
}
