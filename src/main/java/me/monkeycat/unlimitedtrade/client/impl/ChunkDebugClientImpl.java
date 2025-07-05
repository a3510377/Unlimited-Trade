package me.monkeycat.unlimitedtrade.client.impl;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public interface ChunkDebugClientImpl {
    void startWatching(RegistryKey<World> dimension);

    void stopWatching();

    void refresh();

    ChunkDebugDataImpl unlimited_Trade$getChunkData(RegistryKey<World> world, ChunkPos chunkPos);
}
