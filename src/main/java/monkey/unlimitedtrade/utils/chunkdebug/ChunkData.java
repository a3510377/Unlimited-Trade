package monkey.unlimitedtrade.utils.chunkdebug;

import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

public class ChunkData extends BaseChunkData {
    byte statusCode;
    byte ticketCode;

    public ChunkData(ChunkPos chunkPos, ChunkLevelType levelType, byte statusCode, byte ticketCode, Identifier serverWorld) {
        super(chunkPos, levelType, serverWorld);
        this.statusCode = statusCode;
        this.ticketCode = ticketCode;
    }

    public static ChunkData deserialize(Identifier world, long chunkPosition, byte levelType, byte statusType, byte ticketType) {
        return new ChunkData(new ChunkPos(chunkPosition), ChunkLevelType.values()[levelType], statusType, ticketType, world);
    }

    public byte getStatusCode() {
        return statusCode;
    }

    public byte getTicketCode() {
        return ticketCode;
    }
}
