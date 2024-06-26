package monkey.unlimitedtrade.utils.chunkdebug;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static monkey.unlimitedtrade.AutoTradeModClient.LOGGER;

public class ChunkDebugAPI extends BaseChunkDebug {
    public static final Identifier PACKET_ID = new Identifier("essentialclient", "chunkdebug");
    public static final int HELLO = 0,
            DATA = 16,
            VERSION = 1_0_3;

    public final Event<Update> UPDATE_EVENT = EventFactory.createArrayBacked(
            Update.class,
            callbacks -> (world, chunkData) -> Arrays.stream(callbacks).forEach(callback -> callback.onUpdate(world, chunkData))
    );
    private final Map<ChunkPos, ChunkData> worldChunks = new HashMap<>();

    @Nullable
    public ClientPlayNetworkHandler networkHandler;
    @Nullable
    private Identifier currentWorld;

    public ChunkDebugAPI() {
        ClientPlayNetworking.registerGlobalReceiver(PACKET_ID, (client, handler, buf, responseSender) -> this.handlePacket(buf, handler));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> setCurrentWorld(DUMMY_WORLD));
    }

    private void handlePacket(PacketByteBuf buf, ClientPlayNetworkHandler handler) {
        if (buf != null) {
            switch (buf.readVarInt()) {
                case HELLO -> this.onHello(buf, handler);
                case DATA -> this.onData(buf);
            }
        }
    }

    private void onHello(PacketByteBuf buf, ClientPlayNetworkHandler handler) {
        if (buf.readableBytes() == 0 || buf.readVarInt() < VERSION) {
            LOGGER.warn("Server has out of data ChunkDebug");
            return;
        }

        this.networkHandler = handler;
        LOGGER.info("Connected to ChunkDebug server");

        handler.sendPacket(new CustomPayloadC2SPacket(new PacketByteBuf(Unpooled.buffer())
                .writeVarInt(HELLO)
                .writeString("v0.1.0")
                .writeVarInt(VERSION)
        ));
    }

    private void onData(PacketByteBuf buf) {
        int size = buf.readVarInt();

        long[] chunkPositions = buf.readLongArray(new long[size]);
        byte[] levelTypes = buf.readByteArray(size);
        byte[] statusTypes = buf.readByteArray(size);
        byte[] ticketTypes = buf.readByteArray(size);
        String world = buf.readString();

        if (size != chunkPositions.length || size != levelTypes.length || size != statusTypes.length || size != ticketTypes.length) {
            return;
        }

        Identifier worldIdentify = RegistryKey.of(RegistryKeys.WORLD, new Identifier(world)).getValue();
        if (this.currentWorld == null || !this.currentWorld.equals(worldIdentify)) {
            this.currentWorld = worldIdentify;
        }

        for (int i = 0; i < size; i++) {
            ChunkData chunkData = ChunkData.deserialize(worldIdentify, chunkPositions[i], levelTypes[i], statusTypes[i], ticketTypes[i]);
            worldChunks.put(chunkData.getChunkPos(), chunkData);
        }

        this.UPDATE_EVENT.invoker().onUpdate(worldIdentify, worldChunks);
    }

    @Override
    public @Nullable BaseChunkData getChunkData(ChunkPos chunkPos) {
        return this.worldChunks.get(chunkPos);
    }

    @Override
    public void clearChunkData() {
        this.worldChunks.clear();
    }

    @Override
    public void requestChunkData(Identifier world) {
        if (this.networkHandler == null) {
            LOGGER.error("run requestChunkData but networkHandler is null");
            return;
        }

        this.networkHandler.sendPacket(new CustomPayloadC2SPacket(new PacketByteBuf(Unpooled.buffer())
                .writeVarInt(DATA)
                .writeIdentifier(world)
        ));

        super.requestChunkData();
    }

    @FunctionalInterface
    public interface Update {
        void onUpdate(Identifier world, Map<ChunkPos, ChunkData> chunkData);
    }
}
