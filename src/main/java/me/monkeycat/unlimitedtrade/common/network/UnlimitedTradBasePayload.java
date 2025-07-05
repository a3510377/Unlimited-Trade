package me.monkeycat.unlimitedtrade.common.network;

import me.fallenbreath.fanetlib.api.packet.PacketId;
import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import net.minecraft.util.Identifier;

public interface UnlimitedTradBasePayload<P extends UnlimitedTradBasePayload<P>> {
    String PROTOCOL_ID = UnlimitedTradeMod.MOD_ID;

    static <T extends UnlimitedTradBasePayload<T>> PacketId<T> payloadId(String path) {
        return new PacketId<>(Identifier.of(PROTOCOL_ID, path));
    }

    PacketId<P> getId();
}

