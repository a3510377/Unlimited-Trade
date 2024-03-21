package monkey.unlimitedtrade.utils.chunkdebug;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class BaseChunkDebug {
    public static final Identifier DUMMY_WORLD = new Identifier("minecraft:dummy");

    @Nullable
    private Identifier currentWorld;

    public void requestChunkData() {
        this.requestChunkData(DUMMY_WORLD);
    }

    public void requestChunkData(String world) {
        this.requestChunkData(new Identifier(world));
    }

    public void requestChunkData(Identifier world) {
        this.setCurrentWorld(world.equals(DUMMY_WORLD) ? null : world);
    }

    public abstract void clearChunkData();

    public abstract ChunkData getChunkData(ChunkPos chunkPos);

    public @Nullable Identifier getCurrentWorld() {
        return this.currentWorld;
    }

    public void setCurrentWorld(Identifier world) {
        this.currentWorld = world;
        this.clearChunkData();
    }

    public void setCurrentWorld(RegistryKey<World> world) {
        this.currentWorld = world != null ? world.getRegistry() : null;
        this.clearChunkData();
    }
}
