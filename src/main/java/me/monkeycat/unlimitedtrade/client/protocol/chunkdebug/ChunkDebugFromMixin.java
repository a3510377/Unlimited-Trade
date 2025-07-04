package me.monkeycat.unlimitedtrade.client.protocol.chunkdebug;

import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugClientImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugDataImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugDimensionStateImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugMapImpl;
import me.monkeycat.unlimitedtrade.client.protocol.chunkdebug.types.ChunkData;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ChunkDebugFromMixin extends BaseChunkDebugFrom {
    @Nullable
    private static ChunkDebugClientImpl chunkDebugClient;

    public static void setChunkDebugClient(@Nullable ChunkDebugClientImpl client) {
        chunkDebugClient = client;
    }

    @Nullable
    public ChunkDebugMapImpl getMap() {
        return chunkDebugClient != null ? chunkDebugClient.unlimited_Trade$getChunkDebugMap() : null;
    }

    @Nullable
    public ChunkData getChunkData(ChunkPos chunkPos) {
        RegistryKey<World> worldRegistryKey = getCurrentWorld();
        if (worldRegistryKey == null) {
            enabled = null;
            return null;
        }

        ChunkDebugMapImpl chunkDebugMap = getMap();
        if (chunkDebugMap == null) {
            enabled = null;
            return null;
        }

        ChunkDebugDimensionStateImpl dimensionState = chunkDebugMap.unlimited_Trade$getStates().get(worldRegistryKey);
        if (dimensionState == null) {
            enabled = null;
            return null;
        }

        if (dimensionState.unlimited_Trade$getChunks().get(chunkPos.toLong()) instanceof ChunkDebugDataImpl chunkData) {
            enabled = true;
            return ChunkData.fromChunkDebugDataImpl(chunkData, worldRegistryKey.getValue());
        }

        enabled = null;
        return null;
    }

    @Override
    public void startWatching(@Nullable MerchantEntity merchantEntity) {
        if (merchantEntity == null) {
            stopWatching();
            return;
        }

        RegistryKey<World> world = merchantEntity.getWorld().getRegistryKey();
        if (!world.equals(getCurrentWorld())) {
            setCurrentWorld(world);
            if (chunkDebugClient != null) {
                chunkDebugClient.startWatching(world);
            }
        }
    }

    @Override
    public void stopWatching() {
        if (chunkDebugClient != null) {
            chunkDebugClient.stopWatching();
        }

        setCurrentWorld(null);
    }

    @Override
    public boolean canTrade(MerchantEntity merchantEntity) {
        ChunkData data = getChunkData(merchantEntity.getChunkPos());
        ChunkLevelType levelType = (data != null) ? data.levelType() : null;

        if (levelType != null) {
            setStatusChangeFlag(true);
        }

        return levelType == ChunkLevelType.INACCESSIBLE || (levelType == null && getStatusChangeFlag());
    }
}
