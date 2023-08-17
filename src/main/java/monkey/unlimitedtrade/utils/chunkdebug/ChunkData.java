package monkey.unlimitedtrade.utils.chunkdebug;

import net.minecraft.server.world.ChunkHolder.LevelType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

public record ChunkData(
        ChunkPos chunkPos,
        LevelType levelType,
        byte statusCode,
        byte ticketCode,
        Identifier serverWorld
) {
    public static ChunkData deserialize(Identifier world, long chunkPosition, byte levelType, byte statusType, byte ticketType) {
        return new ChunkData(new ChunkPos(chunkPosition), LevelType.values()[levelType], statusType, ticketType, world);
    }
}
