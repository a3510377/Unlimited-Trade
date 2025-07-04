package me.monkeycat.unlimitedtrade.common.network;

import me.monkeycat.unlimitedtrade.common.network.type.CustomRemovalReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record MerchantEntityStatusPayload(RegistryKey<World> world, UUID uuid,
                                          @Nullable Entity.RemovalReason removalReason) implements UnlimitedTradBasePayload {
    public static final Id<MerchantEntityStatusPayload> ID = UnlimitedTradBasePayload.payloadId("entity_status");
    public static final PacketCodec<RegistryByteBuf, MerchantEntityStatusPayload> PACKET_CODEC = PacketCodec.of(MerchantEntityStatusPayload::encode, MerchantEntityStatusPayload::decode);

    private static void encode(MerchantEntityStatusPayload payload, RegistryByteBuf buf) {
        buf.writeRegistryKey(payload.world());
        buf.writeUuid(payload.uuid());
        buf.writeByte(CustomRemovalReason.fromOfficial(payload.removalReason()).code());
    }

    private static MerchantEntityStatusPayload decode(RegistryByteBuf buf) {
        RegistryKey<World> dimension = buf.readRegistryKey(RegistryKeys.WORLD);
        UUID uuid = buf.readUuid();
        byte removeReasonValue = buf.readByte();

        return new MerchantEntityStatusPayload(dimension, uuid, CustomRemovalReason.fromCode(removeReasonValue).official());
    }

    public static MerchantEntityStatusPayload getFromMerchantEntity(MerchantEntity merchantEntity) {
        return new MerchantEntityStatusPayload(merchantEntity.getWorld().getRegistryKey(), merchantEntity.getUuid(), merchantEntity.getRemovalReason());
    }

    @Override
    public Id<MerchantEntityStatusPayload> getId() {
        return ID;
    }
}
