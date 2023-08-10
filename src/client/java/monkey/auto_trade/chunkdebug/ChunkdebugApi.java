package monkey.auto_trade.chunkdebug;

import io.netty.buffer.Unpooled;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import static monkey.auto_trade.AutoTradMod.LOGGER;

public class ChunkdebugApi {
    public static final Identifier PACKET_ID = new Identifier("essentialclient", "chunkdebug");
    public static final int HELLO = 0;
    public static final int DATA = 16;
    public static final int VERSION = 1_0_3;

    @Nullable
    public Identifier listen;
    @Nullable
    private ClientPlayNetworkHandler networkHandler;

    public final void handlePacket(PacketByteBuf packetByteBuf, ClientPlayNetworkHandler networkHandler) {
        if (packetByteBuf != null) {
            switch (packetByteBuf.readVarInt()) {
                case HELLO -> this.onHello(packetByteBuf, networkHandler);
                case DATA -> this.onData(packetByteBuf, networkHandler);
            }
        }
    }

    public final void onHello(PacketByteBuf packetByteBuf, ClientPlayNetworkHandler networkHandler) {
        if (packetByteBuf.readableBytes() == 0 || packetByteBuf.readVarInt() < VERSION) {
            LOGGER.warn("Server has out of data ChunkDebug");
            return;
        }

        this.networkHandler = networkHandler;
        LOGGER.info("Connected to ChunkDebug server");

        networkHandler.sendPacket(new CustomPayloadC2SPacket(
                PACKET_ID,
                new PacketByteBuf(Unpooled.buffer()).writeVarInt(HELLO).writeString("v0.1.0").writeVarInt(VERSION)
        ));
    }

    public final void onData(PacketByteBuf packetByteBuf, ClientPlayNetworkHandler networkHandler) {
        int size = packetByteBuf.readVarInt();

        long[] chunkPositions = packetByteBuf.readLongArray(new long[size]);
        byte[] levelTypes = packetByteBuf.readByteArray(size);
        byte[] statusTypes = packetByteBuf.readByteArray(size);
        byte[] ticketTypes = packetByteBuf.readByteArray(size);
        String world = packetByteBuf.readString();

        ChunkData.deserialize(world, chunkPositions, levelTypes, statusTypes, ticketTypes);
        LOGGER.info(ChunkData.DATA.toString());
    }

    public void requestChunkData() {
        this.requestChunkData("minecraft:dummy");
    }

    public void requestChunkData(String world) {
        this.requestChunkData(new Identifier(world));
    }

    public void requestChunkData(Identifier world) {
        if (this.networkHandler == null) {
            LOGGER.warn("Not yet initialized but using");
            return;
        }

        listen = world.equals(new Identifier("minecraft:dummy")) ? null : world;
        this.networkHandler.sendPacket(new CustomPayloadC2SPacket(
                PACKET_ID,
                new PacketByteBuf(Unpooled.buffer()).writeVarInt(DATA).writeIdentifier(world)
        ));
    }
}
