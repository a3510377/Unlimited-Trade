package me.monkeycat.unlimitedtrade.common.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record UnlimitedTradeHelloPayload(int version) implements UnlimitedTradBasePayload {
    public static final UnlimitedTradeHelloPayload INSTANCE = new UnlimitedTradeHelloPayload(1_0_0);
    public static final Id<UnlimitedTradeHelloPayload> ID = UnlimitedTradBasePayload.payloadId("hello");
    public static final PacketCodec<RegistryByteBuf, UnlimitedTradeHelloPayload> PACKET_CODEC = PacketCodec.of(UnlimitedTradeHelloPayload::encode, UnlimitedTradeHelloPayload::decode);

    private static void encode(UnlimitedTradeHelloPayload payload, RegistryByteBuf buf) {
        buf.writeInt(payload.version());
    }

    private static UnlimitedTradeHelloPayload decode(RegistryByteBuf buf) {
        int version = buf.readInt();

        return new UnlimitedTradeHelloPayload(version);
    }

    @Override
    public Id<UnlimitedTradeHelloPayload> getId() {
        return ID;
    }
}
