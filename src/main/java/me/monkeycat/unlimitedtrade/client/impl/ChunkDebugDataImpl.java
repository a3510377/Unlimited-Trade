package me.monkeycat.unlimitedtrade.client.impl;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;
import org.jetbrains.annotations.Nullable;

public interface ChunkDebugDataImpl {
    ChunkPos unlimited_Trade$position();

    @Nullable ChunkStatus unlimited_Trade$stage();
}
