package me.monkeycat.unlimitedtrade.client.utils.chunkdebug;

import me.monkeycat.unlimitedtrade.client.utils.chunkdebug.types.ChunkData;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class BaseChunkDebugFrom {
    @Nullable
    private RegistryKey<World> currentDimension = null;

    public abstract void startWatching(RegistryKey<World> dimension);

    public abstract void stopWatching();

    public abstract void refresh();

    public abstract @Nullable ChunkData getChunkData(ChunkPos chunkPos);

    public @Nullable RegistryKey<World> getCurrentWorld() {
        return currentDimension;
    }

    public void setCurrentWorld(@Nullable RegistryKey<World> dimension) {
        currentDimension = dimension;
    }
}
