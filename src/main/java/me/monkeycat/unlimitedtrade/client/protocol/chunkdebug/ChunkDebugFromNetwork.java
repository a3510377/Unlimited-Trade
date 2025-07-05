package me.monkeycat.unlimitedtrade.client.protocol.chunkdebug;

import me.monkeycat.unlimitedtrade.common.utils.ModIds;
import net.minecraft.entity.passive.MerchantEntity;

public class ChunkDebugFromNetwork extends BaseChunkDebugFrom {
    public static final String PROTOCOL_ID = ModIds.chunkdebug;

    @Override
    public void startWatching(MerchantEntity merchantEntity) {

    }

    @Override
    public void stopWatching() {

    }

    @Override
    public boolean canTrade(MerchantEntity merchantEntity) {
        return false;
    }
}
