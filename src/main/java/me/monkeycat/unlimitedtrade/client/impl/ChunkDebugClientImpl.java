package me.monkeycat.unlimitedtrade.client.impl;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface ChunkDebugClientImpl {
    @Nullable ChunkDebugMapImpl unlimited_Trade$getChunkDebugMap();

    void startWatching(RegistryKey<World> dimension);

    void stopWatching(RegistryKey<World> dimension);

    void refresh();
}
