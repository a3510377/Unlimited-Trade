package me.monkeycat.unlimitedtrade.client;

import me.monkeycat.unlimitedtrade.client.config.Configs;
import me.monkeycat.unlimitedtrade.client.utils.ModIds;
import me.monkeycat.unlimitedtrade.client.utils.chunkdebug.BaseChunkDebugFrom;
import me.monkeycat.unlimitedtrade.client.utils.chunkdebug.ChunkDebugFromMixin;
import me.monkeycat.unlimitedtrade.client.utils.chunkdebug.ChunkDebugFromNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.util.hit.EntityHitResult;

import static net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.START_CLIENT_TICK;

public class UnlimitedTradeModClient implements ClientModInitializer {
    private static BaseChunkDebugFrom chunkDataAPI;
    private static UnlimitedTradeModClient instance;

    public static BaseChunkDebugFrom getChunkDataAPI() {
        return chunkDataAPI;
    }

    public static UnlimitedTradeModClient getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        instance = this;

        Configs.init();

        if (FabricLoader.getInstance().isModLoaded(ModIds.chunkdebug)) {
            chunkDataAPI = new ChunkDebugFromMixin();
        } else chunkDataAPI = new ChunkDebugFromNetwork();


        START_CLIENT_TICK.register(this::tickHandle);
    }

    public void tickHandle(MinecraftClient client) {
        if (client.crosshairTarget instanceof EntityHitResult hitResult) {
            if (hitResult.getEntity() instanceof MerchantEntity merchantEntity) {
                chunkDataAPI.setCurrentWorld(merchantEntity.getWorld().getRegistryKey());
                System.out.println(chunkDataAPI.getChunkData(merchantEntity.getChunkPos()));
            }
        }
    }
}
