package me.monkeycat.unlimitedtrade.client.protocol;

import net.minecraft.entity.passive.MerchantEntity;
import org.jetbrains.annotations.Nullable;

public abstract class BaseProtocol {
    @Nullable
    protected Boolean enabled = null;
    private boolean statusChangeFlag = false;

    public abstract void startWatching(MerchantEntity merchantEntity);

    public abstract void stopWatching();

    public abstract boolean canTrade(MerchantEntity merchantEntity);

    @Nullable
    public Boolean getEnabled() {
        return enabled;
    }

    public boolean getStatusChangeFlag() {
        return statusChangeFlag;
    }

    public void setStatusChangeFlag(boolean statusChangeFlag) {
        this.statusChangeFlag = statusChangeFlag;
    }
}
