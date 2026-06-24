package foundationgames.enhancedblockentities.client.render;

import foundationgames.enhancedblockentities.event.EBEEvents;

public class BlockEntityRendererOverride {
    public static final BlockEntityRendererOverride NO_OP = new BlockEntityRendererOverride();

    public BlockEntityRendererOverride() {
        EBEEvents.RESOURCE_RELOAD.register(this::onModelsReload);
    }

    public void onModelsReload() {}

}
