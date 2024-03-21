package monkey.unlimitedtrade.utils.chunkdebug.essential;

import monkey.unlimitedtrade.AutoTradeModClient;
import monkey.unlimitedtrade.utils.chunkdebug.BaseChunkDebug;
import monkey.unlimitedtrade.utils.chunkdebug.ChunkData;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EssentialChunkDebugAPI extends BaseChunkDebug {
    @Nullable Class<?> essentialClientClass;
    @Nullable Class<IChunkHandler> chunkHandlerClass;

    @Nullable Method clearAllChunksMethod;
    @Nullable Method getChunksMethod;

    @Nullable Object chunkClientNetworkHandler;
    @Nullable Class<IChunkClientNetworkHandler> chunkClientNetworkHandlerCls;
    @Nullable Method removeChunkDataMethod;
    @Nullable Method requestChunkDataMethod;

    public EssentialChunkDebugAPI() {
        try {
            essentialClientClass = Class.forName("me.senseiwells.essentialclient.EssentialClient");
            //noinspection unchecked
            chunkHandlerClass = (Class<IChunkHandler>) Class.forName("me.senseiwells.essentialclient.feature.chunkdebug.ChunkHandler");

            clearAllChunksMethod = chunkHandlerClass.getMethod("clearAllChunks");
            getChunksMethod = chunkHandlerClass.getMethod("getChunks", RegistryKey.class);

            Field field = essentialClientClass.getDeclaredField("CHUNK_NET_HANDLER");
            chunkClientNetworkHandler = field.get(null);
            //noinspection unchecked
            chunkClientNetworkHandlerCls = (Class<IChunkClientNetworkHandler>) chunkClientNetworkHandler.getClass();
            removeChunkDataMethod = chunkClientNetworkHandlerCls.getMethod("removeChunkData");
            requestChunkDataMethod = chunkClientNetworkHandlerCls.getMethod("requestChunkData", RegistryKey.class);
        } catch (ReflectiveOperationException e) {
            AutoTradeModClient.LOGGER.warn("essentialclient chunkdebug class not found");
        }
    }

    public void clearAllChunks() {
        if (clearAllChunksMethod == null) return;

        try {
            clearAllChunksMethod.invoke(chunkHandlerClass);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ChunkData> objectToChunkData(Identifier world, Object[] chunkData) {
        List<ChunkData> result = new ArrayList<>();

        for (Object data : chunkData) {
            try {
                //noinspection unchecked
                Class<IChunkData> chunkDataCls = (Class<IChunkData>) data.getClass();

                int x = (int) chunkDataCls.getMethod("getPosX").invoke(data);
                int y = (int) chunkDataCls.getMethod("getPosZ").invoke(data);

                result.add(new ChunkData(
                        new ChunkPos(x, y),
                        ChunkLevelType.INACCESSIBLE,
                        (byte) 0,
                        (byte) 0,
                        world));
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    public List<ChunkData> getChunks() {
        if (getChunksMethod == null) return null;

        try {
            return objectToChunkData(getCurrentWorld(), (Object[]) getChunksMethod.invoke(chunkHandlerClass, RegistryKey.of(RegistryKeys.WORLD, getCurrentWorld())));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearChunkData() {
        this.clearAllChunks();
    }

    @Override
    public ChunkData getChunkData(ChunkPos chunkPos) {
        List<ChunkData> chunkData = getChunks();

        for (ChunkData chunk : chunkData) {
            if (chunkPos.equals(chunk.chunkPos())) {
                return chunk;
            }
        }

        return null;
    }

    @Override
    public void requestChunkData(Identifier world) {
        if (this.chunkClientNetworkHandler == null) {
            AutoTradeModClient.LOGGER.error("run EssentialChunkDebug but chunkClientNetworkHandlerClass is null");
            return;
        }

        // if world equals DUMMY_WORLD stop
        try {
            if (world.equals(DUMMY_WORLD)) {
                if (this.removeChunkDataMethod == null) {
                    AutoTradeModClient.LOGGER.error("run EssentialChunkDebug but removeChunkDataMethod is null");
                } else {
                    this.removeChunkDataMethod.invoke(chunkClientNetworkHandler);
                }
            } else if (this.requestChunkDataMethod != null) {
                this.requestChunkDataMethod.invoke(chunkClientNetworkHandler, RegistryKey.of(RegistryKeys.WORLD, world));
            } else {
                AutoTradeModClient.LOGGER.error("run EssentialChunkDebug but requestChunkDataMethod is null");
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        super.requestChunkData(world);
    }
}
