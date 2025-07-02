package me.monkeycat.unlimitedtrade.common.network.type;


import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

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