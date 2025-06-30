package me.monkeycat.unlimitedtrade.client.impl;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public interface ChunkDebugClientImpl {
    ChunkDebugMapImpl unlimited_Trade$getChunkDebugMap();

    void startWatching(RegistryKey<World> dimension);

    void stopWatching();

    void refresh();
}
