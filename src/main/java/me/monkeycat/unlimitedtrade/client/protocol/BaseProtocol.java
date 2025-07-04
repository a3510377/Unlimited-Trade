package me.monkeycat.unlimitedtrade.client.protocol;

import net.minecraft.entity.passive.MerchantEntity;
import org.jetbrains.annotations.Nullable;

public abstract class BaseProtocol {
    @Nullable
    protected Boolean enabled = null;

    public abstract void startWatching(MerchantEntity merchantEntity);

    public abstract void stopWatching();

    public abstract boolean canTrade(MerchantEntity merchantEntity);

    @Nullable
    public Boolean getEnabled() {
        return enabled;
    }
}
