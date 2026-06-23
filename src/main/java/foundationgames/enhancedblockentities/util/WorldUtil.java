package foundationgames.enhancedblockentities.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public enum WorldUtil implements ClientTickEvents.EndLevelTick {
    EVENT_LISTENER;

    private static final Map<Long, ExecutableRunnableHashSet> CHUNK_UPDATE_TASKS = new HashMap<>();
    private static final Map<ResourceKey<Level>, Map<Long, Runnable>> TIMED_TASKS = new HashMap<>();

    @SuppressWarnings("null")
    public static void rebuildChunk(Level world, BlockPos pos) {
        var state = world.getBlockState(pos);
        Minecraft.getInstance().levelRenderer.blockChanged(world, pos, state, state, 8);
    }

    @SuppressWarnings("null")
    public static void rebuildChunkAndThen(Level world, BlockPos pos, Runnable action) {
        CHUNK_UPDATE_TASKS.computeIfAbsent(SectionPos.asLong(pos), k -> new ExecutableRunnableHashSet()).add(action);
        rebuildChunk(world, pos);
    }

    public static void runChunkUpdateTasks(long sectionNode) {
        var task = CHUNK_UPDATE_TASKS.remove(sectionNode);
        if (task != null) {
            task.run();
        }
    }

    public static void scheduleTimed(Level world, long time, Runnable action) {
        TIMED_TASKS.computeIfAbsent(world.dimension(), k -> new HashMap<>()).put(time, action);
    }

    @Override
    public void onEndTick(@SuppressWarnings("null") ClientLevel world) {
        var key = world.dimension();

        if (TIMED_TASKS.containsKey(key)) {
            TIMED_TASKS.get(key).entrySet().removeIf(entry -> {
                if (world.getGameTime() >= entry.getKey()) {
                    entry.getValue().run();
                    return true;
                }

                return false;
            });
        }
    }
}
