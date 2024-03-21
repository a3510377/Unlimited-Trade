package monkey.unlimitedtrade.utils.chunkdebug.essential;

import monkey.unlimitedtrade.utils.chunkdebug.ChunkData;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public interface IChunkHandler {
    ChunkData[] getChunks(RegistryKey<World> world);

    void clearAllChunks();
}
