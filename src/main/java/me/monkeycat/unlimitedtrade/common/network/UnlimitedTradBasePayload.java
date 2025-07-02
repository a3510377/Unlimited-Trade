package me.monkeycat.unlimitedtrade.common.network;

import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public interface UnlimitedTradBasePayload extends CustomPayload {
    String PROTOCOL_ID = UnlimitedTradeMod.MOD_ID;

    static <T extends CustomPayload> Id<T> payloadId(String path) {
        return new Id<>(Identifier.of(PROTOCOL_ID, path));
    }
}
