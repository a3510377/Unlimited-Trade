package me.monkeycat.unlimitedtrade.common.network;

import me.monkeycat.unlimitedtrade.client.utils.chunkdebug.ChunkDebugFromNetwork;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record UnlimitedTradeHelloPayload(int version) implements UnlimitedTradBasePayload {
    public static final UnlimitedTradeHelloPayload INSTANCE = new UnlimitedTradeHelloPayload(1_0_0);
    public static final Id<ChunkDebugFromNetwork.HelloPayloadV3> ID = UnlimitedTradBasePayload.payloadId("hello");
    public static final PacketCodec<RegistryByteBuf, UnlimitedTradeHelloPayload> data = PacketCodec.of(UnlimitedTradeHelloPayload::encode, UnlimitedTradeHelloPayload::decode);

    private static void encode(UnlimitedTradeHelloPayload payload, RegistryByteBuf buf) {
        buf.writeInt(payload.version());
    }

    private static UnlimitedTradeHelloPayload decode(RegistryByteBuf buf) {
        int version = buf.readInt();

        return new UnlimitedTradeHelloPayload(version);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
