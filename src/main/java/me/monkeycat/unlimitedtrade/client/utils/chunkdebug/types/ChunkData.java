package me.monkeycat.unlimitedtrade.client.utils.chunkdebug.types;

import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugDataImpl;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;

public record ChunkData(ChunkPos chunkPos, ChunkStatus levelType, Identifier serverWorld) {
    public static ChunkData fromChunkDebugDataImpl(ChunkDebugDataImpl chunkDebugData, Identifier serverWorld) {
        return new ChunkData(chunkDebugData.unlimited_Trade$position(), chunkDebugData.unlimited_Trade$stage(), serverWorld);
    }
}
