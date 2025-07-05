package me.monkeycat.unlimitedtrade.common.network;

import me.fallenbreath.fanetlib.api.packet.PacketCodec;
import me.fallenbreath.fanetlib.api.packet.PacketId;
import net.minecraft.network.PacketByteBuf;

public record UnlimitedTradeHelloPayload(int version) implements UnlimitedTradBasePayload<UnlimitedTradeHelloPayload> {
    public static final UnlimitedTradeHelloPayload INSTANCE = new UnlimitedTradeHelloPayload(1_0_0);
    public static final PacketId<UnlimitedTradeHelloPayload> ID = UnlimitedTradBasePayload.payloadId("hello");
    public static final PacketCodec<UnlimitedTradeHelloPayload> PACKET_CODEC = PacketCodec.of(UnlimitedTradeHelloPayload::encode, UnlimitedTradeHelloPayload::decode);

    private static void encode(UnlimitedTradeHelloPayload payload, PacketByteBuf buf) {
        buf.writeInt(payload.version());
    }

    private static UnlimitedTradeHelloPayload decode(PacketByteBuf buf) {
        int version = buf.readInt();

        return new UnlimitedTradeHelloPayload(version);
    }

    @Override
    public PacketId<UnlimitedTradeHelloPayload> getId() {
        return ID;
    }
}
