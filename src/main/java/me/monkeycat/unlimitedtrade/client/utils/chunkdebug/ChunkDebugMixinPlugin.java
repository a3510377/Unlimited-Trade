package me.monkeycat.unlimitedtrade.client.utils.chunkdebug;

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;
import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import me.monkeycat.unlimitedtrade.client.utils.ModIds;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixins;

import java.util.List;
import java.util.Set;

public class ChunkDebugMixinPlugin extends RestrictiveMixinConfigPlugin {
    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {
    }

    @Override
    public List<String> getMixins() {
        if (FabricLoader.getInstance().isModLoaded(ModIds.chunkdebug)) {
            UnlimitedTradeMod.LOGGER.info("loading ChunkDebug mixins");
            Mixins.addConfiguration("unlimitedtrademod.chunkdebug.mixins.json");
        }

        return null;
    }
}
