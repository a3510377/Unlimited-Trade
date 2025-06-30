package me.monkeycat.unlimitedtrade.client.utils.chunkdebug;

import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import me.monkeycat.unlimitedtrade.client.utils.ModIds;
import me.monkeycat.unlimitedtrade.client.utils.chunkdebug.types.ChunkData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ChunkDebugFromNetwork extends BaseChunkDebugFrom {
    public static final String PROTOCOL_ID = ModIds.chunkdebug;
    public static final int PROTOCOL_VERSION_V3 = 3;

    private static <T extends CustomPayload> CustomPayload.Id<T> payloadId(String path) {
        return new CustomPayload.Id<>(Identifier.of(PROTOCOL_ID, path));
    }

    private void handleHello(HelloPayloadV3 payload, ClientPlayNetworking.Context context) {
        UnlimitedTradeMod.LOGGER.info("Chunk debug protocol version: {}", payload.version());
        if (payload.version() == PROTOCOL_VERSION_V3) {
        }
    }

    @Override
    public void startWatching(RegistryKey<World> dimension) {
    }

    @Override
    public void stopWatching() {
    }

    @Override
    public void refresh() {
    }

    @Override
    public @Nullable ChunkData getChunkData(ChunkPos chunkPos) {
        return null;
    }

    public record HelloPayloadV3(int version) implements CustomPayload {
        public static final Id<HelloPayloadV3> ID = payloadId("hello");

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
