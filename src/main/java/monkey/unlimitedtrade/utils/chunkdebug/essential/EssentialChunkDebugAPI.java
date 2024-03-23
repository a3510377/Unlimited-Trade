package monkey.unlimitedtrade.utils.chunkdebug.essential;

import monkey.unlimitedtrade.AutoTradeModClient;
import monkey.unlimitedtrade.utils.chunkdebug.BaseChunkData;
import monkey.unlimitedtrade.utils.chunkdebug.BaseChunkDebug;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EssentialChunkDebugAPI extends BaseChunkDebug {
    @Nullable Class<?> essentialClientClass;
    @Nullable Class<IChunkHandler> chunkHandlerClass;
    @Nullable Class<IChunkTypeEnum> chunkTypeEnumClass;
    Map<Object, Integer> chunkTypeEnumMaps = new HashMap<>();

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
            //noinspection unchecked
            chunkTypeEnumClass = (Class<IChunkTypeEnum>) Class.forName("me.senseiwells.essentialclient.feature.chunkdebug.ChunkType");
            if (chunkTypeEnumClass.isEnum()) {
                Object[] values = (Object[]) chunkTypeEnumClass.getMethod("values").invoke(chunkTypeEnumClass);

                int i = 0;
                for (Object value : values) {
                    chunkTypeEnumMaps.put(value, i++);
                }
            }

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

    public BaseChunkData objectToChunkData(Identifier world, Object chunkData) {
        try {
            //noinspection unchecked
            Class<IChunkData> chunkDataCls = (Class<IChunkData>) chunkData.getClass();

            int x = (int) chunkDataCls.getMethod("getPosX").invoke(chunkData);
            int y = (int) chunkDataCls.getMethod("getPosZ").invoke(chunkData);

            Object chunkTypeClass = chunkDataCls.getMethod("getChunkType").invoke(chunkData);
            int chunkLevelTypeIndex = chunkTypeEnumMaps.getOrDefault(chunkTypeClass, -1);
            return new BaseChunkData(new ChunkPos(x, y), ChunkLevelType.values()[chunkLevelTypeIndex], world);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public Object[] getChunks() {
        if (getChunksMethod == null) return null;

        try {
            return (Object[]) getChunksMethod.invoke(chunkHandlerClass, RegistryKey.of(RegistryKeys.WORLD, getCurrentWorld()));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearChunkData() {
        this.clearAllChunks();
    }

    @Override
    public @Nullable BaseChunkData getChunkData(ChunkPos chunkPos) {
        for (Object chunkObject : getChunks()) {
            BaseChunkData chunkData = objectToChunkData(getCurrentWorld(), chunkObject);

            if (chunkPos.equals(chunkData.getChunkPos())) {
                return chunkData;
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
                    AutoTradeModClient.LOGGER.debug("remove chunk data of world: {}", world);
                    this.removeChunkDataMethod.invoke(chunkClientNetworkHandler);
                }
            } else if (this.requestChunkDataMethod != null) {
                AutoTradeModClient.LOGGER.debug("request chunk data of world: {}", world);
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
