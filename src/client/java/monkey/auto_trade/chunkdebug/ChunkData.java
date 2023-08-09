package monkey.auto_trade.chunkdebug;

import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public record ChunkData(ChunkPos chunkPos, ChunkLevelType levelType, byte statusCode, byte ticketCode) {
    public static final Map<String, ChunkData[]> DATA = new HashMap<>();

    public static void deserialize(String world, long[] chunkPositions, byte[] levelTypes, byte[] statusTypes, byte[] ticketTypes) {
        int size = chunkPositions.length;

        if (size != levelTypes.length || size != statusTypes.length || size != ticketTypes.length) {
            return;
        }

        ArrayList<ChunkData> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new ChunkData(
                    new ChunkPos(chunkPositions[i]),
                    ChunkLevelType.values()[levelTypes[i]],
                    statusTypes[i],
                    ticketTypes[i]
            ));
        }

        ChunkData.DATA.put(world, list.toArray(new ChunkData[0]));
    }

    public int getPosX() {
        return this.chunkPos.x;
    }

    public int getPosZ() {
        return this.chunkPos.z;
    }
}
