package monkey.unlimitedtrade.utils.chunkdebug;

import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

public class BaseChunkData {
    private final ChunkPos chunkPos;
    private final ChunkLevelType levelType;
    private final Identifier serverWorld;

    public BaseChunkData(ChunkPos chunkPos, ChunkLevelType levelType, Identifier serverWorld) {
        this.chunkPos = chunkPos;
        this.levelType = levelType;
        this.serverWorld = serverWorld;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public ChunkLevelType getLevelType() {
        return levelType;
    }

    public Identifier getServerWorld() {
        return serverWorld;
    }
}
