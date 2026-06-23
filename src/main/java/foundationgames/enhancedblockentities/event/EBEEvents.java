package foundationgames.enhancedblockentities.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public enum EBEEvents {;
    @SuppressWarnings("null")
    public static final Event<Runnable> RESOURCE_RELOAD = EventFactory.createArrayBacked(Runnable.class, (callbacks) -> () -> {
        for (var event : callbacks) {
            event.run();
        }
    });
}
