package me.monkeycat.unlimitedtrade.common.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.util.UUID;

public record UnlimitedTradeStopWatchPayload(UUID uuid) implements UnlimitedTradBasePayload {
    public static final Id<UnlimitedTradeStopWatchPayload> ID = UnlimitedTradBasePayload.payloadId("stop_watch");
    public static final PacketCodec<RegistryByteBuf, UnlimitedTradeStopWatchPayload> PACKET_CODEC = PacketCodec.of(UnlimitedTradeStopWatchPayload::encode, UnlimitedTradeStopWatchPayload::decode);

    private static void encode(UnlimitedTradeStopWatchPayload payload, RegistryByteBuf buf) {
        buf.writeUuid(payload.uuid());
    }

    private static UnlimitedTradeStopWatchPayload decode(RegistryByteBuf buf) {
        UUID uuid = buf.readUuid();

        return new UnlimitedTradeStopWatchPayload(uuid);
    }

    @Override
    public Id<UnlimitedTradeStopWatchPayload> getId() {
        return ID;
    }
}
