package monkey.auto_trade.chunkdebug;

import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.util.math.ChunkPos;

import java.util.HashMap;
import java.util.Map;

public record ChunkData(ChunkPos chunkPos, ChunkLevelType levelType, byte statusCode, byte ticketCode) {
    public static final Map<String, Map<ChunkPos, ChunkData>> serverWorldChunks = new HashMap<>();

    public static void deserialize(String world, long[] chunkPositions, byte[] levelTypes, byte[] statusTypes, byte[] ticketTypes) {
        int size = chunkPositions.length;

        if (size != levelTypes.length || size != statusTypes.length || size != ticketTypes.length) {
            return;
        }

        Map<ChunkPos, ChunkData> chunkDataMap = serverWorldChunks.getOrDefault(world, new HashMap<>());
        for (int i = 0; i < size; i++) {
            ChunkPos chunkPos = new ChunkPos(chunkPositions[i]);
            chunkDataMap.put(chunkPos, new ChunkData(
                    chunkPos,
                    ChunkLevelType.values()[levelTypes[i]],
                    statusTypes[i],
                    ticketTypes[i]
            ));
        }

        serverWorldChunks.put(world, chunkDataMap);
    }

    public int getPosX() {
        return this.chunkPos.x;
    }

    public int getPosZ() {
        return this.chunkPos.z;
    }
}
