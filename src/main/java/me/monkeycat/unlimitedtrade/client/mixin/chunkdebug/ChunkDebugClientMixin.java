package me.monkeycat.unlimitedtrade.client.mixin.chunkdebug;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.monkeycat.unlimitedtrade.UnlimitedTradeMod;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugClientImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugMapImpl;
import me.monkeycat.unlimitedtrade.client.utils.ModIds;
import me.monkeycat.unlimitedtrade.client.utils.chunkdebug.ChunkDebugFromMixin;
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
        try {
            Field field = this.getClass().getDeclaredField("map");
            field.setAccessible(true);

            chunkDebugMap = (ChunkDebugMapImpl) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            UnlimitedTradeMod.LOGGER.error("Failed to access 'map' field in ChunkDebugClient: ", e);
        }
    }

    public @Nullable ChunkDebugMapImpl unlimited_Trade$getChunkDebugMap() {
        return chunkDebugMap;
    }
}
