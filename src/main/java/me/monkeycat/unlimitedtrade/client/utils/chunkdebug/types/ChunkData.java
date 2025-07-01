package me.monkeycat.unlimitedtrade.client.utils.chunkdebug.types;

import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugDataImpl;
import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

public record ChunkData(ChunkPos chunkPos, ChunkLevelType levelType, Identifier serverWorld) {
    public static ChunkData fromChunkDebugDataImpl(ChunkDebugDataImpl chunkDebugData, Identifier serverWorld) {
        return new ChunkData(chunkDebugData.position(), chunkDebugData.status(), serverWorld);
    }
}
