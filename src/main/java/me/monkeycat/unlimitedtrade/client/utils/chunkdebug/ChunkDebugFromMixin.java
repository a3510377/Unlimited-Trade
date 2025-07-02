package me.monkeycat.unlimitedtrade.client.utils.chunkdebug;

import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugClientImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugDataImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugDimensionStateImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugMapImpl;
import me.monkeycat.unlimitedtrade.client.utils.chunkdebug.types.ChunkData;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ChunkDebugFromMixin extends BaseChunkDebugFrom {
    @Nullable
    private static ChunkDebugClientImpl chunkDebugClient;

    public static void setChunkDebugClient(@Nullable ChunkDebugClientImpl chunkDebugClient) {
        ChunkDebugFromMixin.chunkDebugClient = chunkDebugClient;
    }

    @Nullable
    public ChunkDebugMapImpl getMap() {
        return chunkDebugClient != null ? chunkDebugClient.unlimited_Trade$getChunkDebugMap() : null;
    }

    @Override
    public void startWatching(RegistryKey<World> dimension) {
        if (chunkDebugClient != null) {
            chunkDebugClient.startWatching(dimension);
        }
    }

    @Override
    public void stopWatching() {
        // Check chunkdebug map is not use
        if (chunkDebugClient != null && chunkDebugClient.unlimited_Trade$getChunkDebugMap() == null) {
            chunkDebugClient.stopWatching();
        }
    }

    @Override
    public void refresh() {
        if (chunkDebugClient != null) {
            chunkDebugClient.refresh();
        }
    }

    @Override
    public void setCurrentWorld(@Nullable RegistryKey<World> dimension) {
        if (getCurrentWorld() != dimension) {
            startWatching(dimension);
            super.setCurrentWorld(dimension);
        }
    }

    @Override
    public @Nullable ChunkData getChunkData(ChunkPos chunkPos) {
        RegistryKey<World> worldRegistryKey = getCurrentWorld();
        if (worldRegistryKey == null) {
            enabled = null;
            return null;
        }

        ChunkDebugMapImpl chunkDebugMap = getMap();
        if (chunkDebugMap == null) {
            enabled = null;
            return null;
        }

        Object state = chunkDebugMap.unlimited_Trade$getStates().get(worldRegistryKey);
        if (state instanceof ChunkDebugDimensionStateImpl dimensionState) {
            if (dimensionState.unlimited_Trade$getChunks().get(chunkPos.toLong()) instanceof ChunkDebugDataImpl chunkData) {
                enabled = true;
                return ChunkData.fromChunkDebugDataImpl(chunkData, worldRegistryKey.getValue());
            }
        }

        enabled = null;
        return null;
    }
}
