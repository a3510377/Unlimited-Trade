package me.monkeycat.unlimitedtrade.client.mixin.chunkdebug;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugBaseGetStatusImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugClientImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugDataImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugDimensionStateImpl;
import me.monkeycat.unlimitedtrade.client.protocol.chunkdebug.ChunkDebugFromMixin;
import me.monkeycat.unlimitedtrade.common.utils.ModIds;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

//#if MC >= 12100
//$$ import java.lang.reflect.Field;
//$$ import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
//#endif

@Restriction(require = @Condition(ModIds.chunkdebug))
@Pseudo
@Mixin(targets = "me.senseiwells.chunkdebug.client.ChunkDebugClient")
public abstract class ChunkDebugClientMixin implements ChunkDebugClientImpl {
    //#if MC >= 12100
    //$$ @Unique
    //$$ @Nullable
    //$$ private ChunkDebugBaseGetStatusImpl chunkDebugMap;
    //$$
    //$$ @Inject(method = "setChunkMap", at = @At("RETURN"))
    //$$ private void setChunkDebugMapMixin(CallbackInfo ci) {
    //$$     try {
    //$$         Class<?> clazz = Class.forName("me.senseiwells.chunkdebug.client.ChunkDebugClient");
    //$$         Field field = clazz.getDeclaredField("map");
    //$$         field.setAccessible(true);
    //$$
    //$$         Object value = field.get(this);
    //$$         if (value instanceof ChunkDebugMapImpl impl) this.chunkDebugMap = impl;
    //$$         else if (value == null) this.chunkDebugMap = null;
    //$$         else UnlimitedTradeMod.LOGGER.error("Chunk debug map type is not supper!");
    //$$     } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
    //$$         UnlimitedTradeMod.LOGGER.error("Failed to access 'map' field in ChunkDebugClient: ", e);
    //$$     }
    //$$ }
    //$$
    //$$ @Unique
    //$$ public ChunkDebugMapImpl getMap() {
    //$$     return chunkDebugMap;
    //$$ }
    //#else
    @Unique
    @Nullable
    private ChunkDebugBaseGetStatusImpl baseScreen;

    @Inject(method = "onInitializeClient", at = @At("TAIL"))
    private void onInitializeClientMixin(CallbackInfo ci) {
        ChunkDebugFromMixin.setChunkDebugClient(this);
    }

    @Inject(method = "handleHello", at = @At("TAIL"))
    private void handleHelloMixin(CallbackInfo ci) {
        try {
            Field field = this.getClass().getDeclaredField("baseScreen");
            field.setAccessible(true);

            Object value = field.get(this);
            if (value instanceof ChunkDebugBaseGetStatusImpl) this.baseScreen = (ChunkDebugBaseGetStatusImpl) value;
            else if (value == null) this.baseScreen = null;
            else UnlimitedTradeMod.LOGGER.error("Chunk debug screen type is not supported!");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            UnlimitedTradeMod.LOGGER.error("Failed to access 'screen' field in ChunkDebugClient: ", e);
            this.baseScreen = null; // Fallback to null if we can't access the field
        }
    }
    //#endif

    @Nullable
    public ChunkDebugDataImpl unlimited_Trade$getChunkData(RegistryKey<World> world, ChunkPos chunkPos) {
        //#if MC >= 12100
        //$$ ChunkDebugDimensionStateImpl chunkData = chunkDebugMap.unlimited_Trade$getStates().get(world);
        //#endif MC >= 12100
        if (this.baseScreen == null) {
            return null;
        }

        Object stateObj = this.baseScreen.unlimited_Trade$getStates().get(world);
        if (!(stateObj instanceof ChunkDebugDimensionStateImpl state)) {
            return null;
        }

        if (state.unlimited_Trade$getChunks().get(chunkPos.toLong()) instanceof ChunkDebugDataImpl chunkData) {
            return chunkData;
        }

        return null;
    }
}
