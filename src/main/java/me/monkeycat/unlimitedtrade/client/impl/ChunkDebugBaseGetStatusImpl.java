package me.monkeycat.unlimitedtrade.client.impl;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Map;

public interface ChunkDebugBaseGetStatusImpl {
    // Object is ChunkDebugDimensionStateImpl
    Map<RegistryKey<World>, Object> unlimited_Trade$getStates();
}
