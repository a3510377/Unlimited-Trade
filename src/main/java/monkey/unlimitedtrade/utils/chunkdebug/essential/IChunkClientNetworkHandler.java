package monkey.unlimitedtrade.utils.chunkdebug.essential;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public interface IChunkClientNetworkHandler {
    int getVersion();

    void removeChunkData();

    void requestChunkData(RegistryKey<World> world);
}
