package me.monkeycat.unlimitedtrade.common.utils.mixin;

import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import me.monkeycat.unlimitedtrade.common.utils.ModIds;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixins;

import java.util.List;

public class UnlimitedTradeClientMixinPlugin extends UnlimitedTradeBaseMixinPlugin {
    @Override
    public List<String> getMixins() {
        if (FabricLoader.getInstance().isModLoaded(ModIds.chunkdebug)) {
            UnlimitedTradeMod.LOGGER.info("loading ChunkDebug mixins");
            Mixins.addConfiguration("unlimitedtrademod.chunkdebug.mixins.json");
        }

        return null;
    }
}
