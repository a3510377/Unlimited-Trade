package me.monkeycat.unlimitedtrade.client.impl;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public interface ChunkDebugMapImpl {
    Map<RegistryKey<World>, ChunkDebugDataImpl> unlimited_Trade$getStates();

    List<RegistryKey<World>> unlimited_Trade$getDimensions();
}
