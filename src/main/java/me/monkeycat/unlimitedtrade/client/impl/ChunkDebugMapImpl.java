package me.monkeycat.unlimitedtrade.client.impl;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Map;

public interface ChunkDebugMapImpl {
    Map<RegistryKey<World>, ChunkDebugDimensionStateImpl> unlimited_Trade$getStates();
}
