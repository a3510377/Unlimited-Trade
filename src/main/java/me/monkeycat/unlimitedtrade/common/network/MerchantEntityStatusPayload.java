package me.monkeycat.unlimitedtrade.common.network;

import me.monkeycat.unlimitedtrade.client.utils.chunkdebug.ChunkDebugFromNetwork;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public record MerchantEntityStatusPayload(RegistryKey<World> world, int entityId,
                                          @Nullable Entity.RemovalReason removalReason) implements UnlimitedTradBasePayload {
    public static final Id<ChunkDebugFromNetwork.HelloPayloadV3> ID = UnlimitedTradBasePayload.payloadId("entity_status");
    public static final PacketCodec<RegistryByteBuf, MerchantEntityStatusPayload> data = PacketCodec.of(MerchantEntityStatusPayload::encode, MerchantEntityStatusPayload::decode);

    private static void encode(MerchantEntityStatusPayload payload, RegistryByteBuf buf) {
        buf.writeRegistryKey(payload.world);
        buf.writeVarInt(payload.entityId);
        buf.writeByte(CustomRemovalReason.fromOfficial(payload.removalReason()).code());
    }

    private static MerchantEntityStatusPayload decode(RegistryByteBuf buf) {
        RegistryKey<World> dimension = buf.readRegistryKey(RegistryKeys.WORLD);
        int entityId = buf.readVarInt();
        byte removeReasonValue = buf.readByte();

        return new MerchantEntityStatusPayload(dimension, entityId, CustomRemovalReason.fromCode(removeReasonValue).official());
    }

    public static MerchantEntityStatusPayload getFromMerchantEntity(MerchantEntity merchantEntity) {
        return new MerchantEntityStatusPayload(merchantEntity.getWorld().getRegistryKey(), merchantEntity.getId(), merchantEntity.getRemovalReason());
    }

    @Nullable
    public MerchantEntity getMerchantEntity(ServerWorld serverWorld) {
        if (serverWorld.getEntityById(entityId) instanceof MerchantEntity merchantEntity) {
            return merchantEntity;
        }

        return null;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public enum CustomRemovalReason {
        NONE((byte) 0, null), KILLED((byte) 1, Entity.RemovalReason.KILLED), DISCARDED((byte) 2, Entity.RemovalReason.DISCARDED), UNLOADED_TO_CHUNK((byte) 3, Entity.RemovalReason.UNLOADED_TO_CHUNK), UNLOADED_WITH_PLAYER((byte) 4, Entity.RemovalReason.UNLOADED_WITH_PLAYER), CHANGED_DIMENSION((byte) 5, Entity.RemovalReason.CHANGED_DIMENSION);

        private static final Map<Byte, CustomRemovalReason> BY_CODE = new HashMap<>();
        private static final Map<Entity.RemovalReason, CustomRemovalReason> BY_OFFICIAL = new EnumMap<>(Entity.RemovalReason.class);

        static {
            for (CustomRemovalReason r : values()) {
                BY_CODE.put(r.code, r);

                if (r.officialRemoveReason != null) {
                    BY_OFFICIAL.put(r.officialRemoveReason, r);
                }
            }
        }

        private final byte code;
        private final Entity.RemovalReason officialRemoveReason;

        CustomRemovalReason(byte code, Entity.RemovalReason officialRemoveReason) {
            this.code = code;
            this.officialRemoveReason = officialRemoveReason;
        }

        public static CustomRemovalReason fromCode(byte code) {
            return BY_CODE.getOrDefault(code, NONE);
        }

        public static CustomRemovalReason fromOfficial(@Nullable Entity.RemovalReason officialRemoveReason) {
            return officialRemoveReason != null ? BY_OFFICIAL.getOrDefault(officialRemoveReason, NONE) : NONE;
        }

        public byte code() {
            return code;
        }

        public @Nullable Entity.RemovalReason official() {
            return officialRemoveReason;
        }
    }
}
