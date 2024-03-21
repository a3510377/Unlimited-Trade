package monkey.unlimitedtrade.utils.chunkdebug;

import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

public record ChunkData(
        ChunkPos chunkPos,
        ChunkLevelType levelType,
        byte statusCode,
        byte ticketCode,
        Identifier serverWorld
) {
    public static ChunkData deserialize(Identifier world, long chunkPosition, byte levelType, byte statusType, byte ticketType) {
        return new ChunkData(new ChunkPos(chunkPosition), ChunkLevelType.values()[levelType], statusType, ticketType, world);
    }
}


//public ChunkPos(long pos) {
//    this.x = (int)pos;
//    this.z = (int)(pos >> 32);
//}