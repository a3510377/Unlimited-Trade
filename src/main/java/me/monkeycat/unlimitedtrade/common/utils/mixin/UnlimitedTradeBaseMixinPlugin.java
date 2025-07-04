package me.monkeycat.unlimitedtrade.common.utils.mixin;

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;
import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;

import java.util.List;
import java.util.Set;

public class UnlimitedTradeBaseMixinPlugin extends RestrictiveMixinConfigPlugin {
    @Override
    protected void onRestrictionCheckFailed(String mixinClassName, String reason) {
        UnlimitedTradeMod.LOGGER.info("Disabled mixin {} due to {}", mixinClassName, reason);
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }
}
