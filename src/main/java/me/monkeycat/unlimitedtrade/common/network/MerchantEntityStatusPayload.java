package me.monkeycat.unlimitedtrade.common.network;

import me.fallenbreath.fanetlib.api.packet.PacketCodec;
import me.fallenbreath.fanetlib.api.packet.PacketId;
import me.monkeycat.unlimitedtrade.common.network.type.CustomRemovalReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record MerchantEntityStatusPayload(RegistryKey<World> world, UUID uuid,
                                          @Nullable Entity.RemovalReason removalReason) implements UnlimitedTradBasePayload<MerchantEntityStatusPayload> {
    public static final PacketId<MerchantEntityStatusPayload> ID = UnlimitedTradBasePayload.payloadId("entity_status");
    public static final PacketCodec<MerchantEntityStatusPayload> PACKET_CODEC = PacketCodec.of(MerchantEntityStatusPayload::encode, MerchantEntityStatusPayload::decode);

    private static void encode(MerchantEntityStatusPayload payload, PacketByteBuf buf) {
        buf.writeRegistryKey(payload.world());
        buf.writeUuid(payload.uuid());
        buf.writeByte(CustomRemovalReason.fromOfficial(payload.removalReason()).code());
    }

    private static MerchantEntityStatusPayload decode(PacketByteBuf buf) {
        RegistryKey<World> dimension = buf.readRegistryKey(RegistryKeys.WORLD);
        UUID uuid = buf.readUuid();
        byte removeReasonValue = buf.readByte();

        return new MerchantEntityStatusPayload(dimension, uuid, CustomRemovalReason.fromCode(removeReasonValue).official());
    }

    public static MerchantEntityStatusPayload getFromMerchantEntity(MerchantEntity merchantEntity) {
        return new MerchantEntityStatusPayload(merchantEntity.getWorld().getRegistryKey(), merchantEntity.getUuid(), merchantEntity.getRemovalReason());
    }

    @Override
    public PacketId<MerchantEntityStatusPayload> getId() {
        return ID;
    }
}
