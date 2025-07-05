package me.monkeycat.unlimitedtrade.common.network;

import me.fallenbreath.fanetlib.api.packet.PacketCodec;
import me.fallenbreath.fanetlib.api.packet.PacketId;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

import java.util.UUID;

public record UnlimitedTradeStartWatchPayload(RegistryKey<World> world,
                                              UUID uuid) implements UnlimitedTradBasePayload<UnlimitedTradeStartWatchPayload> {
    public static final PacketId<UnlimitedTradeStartWatchPayload> ID = UnlimitedTradBasePayload.payloadId("start_watch");
    public static final PacketCodec<UnlimitedTradeStartWatchPayload> PACKET_CODEC = PacketCodec.of(UnlimitedTradeStartWatchPayload::encode, UnlimitedTradeStartWatchPayload::decode);

    private static void encode(UnlimitedTradeStartWatchPayload payload, PacketByteBuf buf) {
        buf.writeRegistryKey(payload.world());
        buf.writeUuid(payload.uuid());
    }

    private static UnlimitedTradeStartWatchPayload decode(PacketByteBuf buf) {
        RegistryKey<World> world = buf.readRegistryKey(RegistryKeys.WORLD);
        UUID uuid = buf.readUuid();

        return new UnlimitedTradeStartWatchPayload(world, uuid);
    }

    @Override
    public PacketId<UnlimitedTradeStartWatchPayload> getId() {
        return ID;
    }
}
