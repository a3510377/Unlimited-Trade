package me.monkeycat.unlimitedtrade.client.protocol.chunkdebug;

import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import me.monkeycat.unlimitedtrade.common.utils.ModIds;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class ChunkDebugFromNetwork extends BaseChunkDebugFrom {
    public static final String PROTOCOL_ID = ModIds.chunkdebug;
    public static final int PROTOCOL_VERSION_V3 = 3;

    private static <T extends CustomPayload> CustomPayload.Id<T> payloadId(String path) {
        return new CustomPayload.Id<>(Identifier.of(PROTOCOL_ID, path));
    }

    private void handleHello(HelloPayloadV3 payload, ClientPlayNetworking.Context context) {
        UnlimitedTradeMod.LOGGER.info("Chunk debug protocol version: {}", payload.version());
        if (payload.version() == PROTOCOL_VERSION_V3) {
        }
    }

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

    public record HelloPayloadV3(int version) implements CustomPayload {
        public static final Id<HelloPayloadV3> ID = payloadId("hello");

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
