package me.monkeycat.unlimitedtrade.client.impl;

import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.Nullable;

public interface ChunkDebugDataImpl {
    ChunkPos position();

    @Nullable ChunkLevelType status();
}
