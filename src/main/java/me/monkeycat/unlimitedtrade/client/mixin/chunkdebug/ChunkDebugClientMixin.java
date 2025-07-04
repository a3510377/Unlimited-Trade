package me.monkeycat.unlimitedtrade.client.mixin.chunkdebug;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugClientImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugMapImpl;
import me.monkeycat.unlimitedtrade.client.protocol.chunkdebug.ChunkDebugFromMixin;
import me.monkeycat.unlimitedtrade.common.utils.ModIds;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Restriction(require = @Condition(ModIds.chunkdebug))
@Pseudo
@Mixin(targets = "me.senseiwells.chunkdebug.client.ChunkDebugClient")
public abstract class ChunkDebugClientMixin implements ChunkDebugClientImpl {
    @Unique
    @Nullable
    private ChunkDebugMapImpl chunkDebugMap;

    @Inject(method = "onInitializeClient", at = @At("TAIL"))
    private void onInitializeClientMixin(CallbackInfo ci) {
        ChunkDebugFromMixin.setChunkDebugClient(this);
    }

    @Inject(method = "setChunkMap", at = @At("RETURN"))
    private void setChunkDebugMapMixin(CallbackInfo ci) {
        try {
            Class<?> clazz = Class.forName("me.senseiwells.chunkdebug.client.ChunkDebugClient");
            Field field = clazz.getDeclaredField("map");
            field.setAccessible(true);

            Object value = field.get(this);
            if (value instanceof ChunkDebugMapImpl impl) this.chunkDebugMap = impl;
            else if (value == null) this.chunkDebugMap = null;
            else UnlimitedTradeMod.LOGGER.error("Chunk debug map type is not supper!");
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            UnlimitedTradeMod.LOGGER.error("Failed to access 'map' field in ChunkDebugClient: ", e);
        }
    }

    public @Nullable ChunkDebugMapImpl unlimited_Trade$getChunkDebugMap() {
        return chunkDebugMap;
    }
}
