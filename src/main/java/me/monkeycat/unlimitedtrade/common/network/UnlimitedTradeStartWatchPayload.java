package me.monkeycat.unlimitedtrade.common.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

import java.util.UUID;

public record UnlimitedTradeStartWatchPayload(RegistryKey<World> world,
                                              UUID uuid) implements UnlimitedTradBasePayload {
    public static final Id<UnlimitedTradeStartWatchPayload> ID = UnlimitedTradBasePayload.payloadId("start_watch");
    public static final PacketCodec<RegistryByteBuf, UnlimitedTradeStartWatchPayload> PACKET_CODEC = PacketCodec.of(UnlimitedTradeStartWatchPayload::encode, UnlimitedTradeStartWatchPayload::decode);

    private static void encode(UnlimitedTradeStartWatchPayload payload, RegistryByteBuf buf) {
        buf.writeRegistryKey(payload.world());
        buf.writeUuid(payload.uuid());
    }

    private static UnlimitedTradeStartWatchPayload decode(RegistryByteBuf buf) {
        RegistryKey<World> world = buf.readRegistryKey(RegistryKeys.WORLD);
        UUID uuid = buf.readUuid();

        return new UnlimitedTradeStartWatchPayload(world, uuid);
    }

    @Override
    public Id<UnlimitedTradeStartWatchPayload> getId() {
        return ID;
    }
}
