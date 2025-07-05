package me.monkeycat.unlimitedtrade.common.network;

import me.fallenbreath.fanetlib.api.packet.PacketCodec;
import me.fallenbreath.fanetlib.api.packet.PacketId;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public record UnlimitedTradeStopWatchPayload(
        UUID uuid) implements UnlimitedTradBasePayload<UnlimitedTradeStopWatchPayload> {
    public static final PacketId<UnlimitedTradeStopWatchPayload> ID = UnlimitedTradBasePayload.payloadId("stop_watch");
    public static final PacketCodec<UnlimitedTradeStopWatchPayload> PACKET_CODEC = PacketCodec.of(UnlimitedTradeStopWatchPayload::encode, UnlimitedTradeStopWatchPayload::decode);

    private static void encode(UnlimitedTradeStopWatchPayload payload, PacketByteBuf buf) {
        buf.writeUuid(payload.uuid());
    }

    private static UnlimitedTradeStopWatchPayload decode(PacketByteBuf buf) {
        UUID uuid = buf.readUuid();

        return new UnlimitedTradeStopWatchPayload(uuid);
    }

    @Override
    public PacketId<UnlimitedTradeStopWatchPayload> getId() {
        return ID;
    }
}
