package me.monkeycat.unlimitedtrade.client.protocol.chunkdebug;

import me.monkeycat.unlimitedtrade.client.protocol.BaseProtocol;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class BaseChunkDebugFrom extends BaseProtocol {
    @Nullable
    private RegistryKey<World> currentWorld;

    @Nullable
    public RegistryKey<World> getCurrentWorld() {
        return currentWorld;
    }

    public void setCurrentWorld(@Nullable RegistryKey<World> currentWorld) {
        this.currentWorld = currentWorld;
    }
}
