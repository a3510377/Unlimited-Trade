package me.monkeycat.unlimitedtrade.client.mixin.chunkdebug;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugClientImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugMapImpl;
import me.monkeycat.unlimitedtrade.client.utils.ModIds;
import me.monkeycat.unlimitedtrade.client.utils.chunkdebug.ChunkDebugFromMixin;
import me.senseiwells.chunkdebug.client.gui.ChunkDebugMap;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(ModIds.chunkdebug))
@Pseudo
@Mixin(targets = "me.senseiwells.chunkdebug.client.ChunkDebugClient")
public abstract class ChunkDebugClientMixin implements ChunkDebugClientImpl {
    @Shadow(remap = false)
    @Nullable
    private ChunkDebugMap map;

    @Inject(method = "onInitializeClient", at = @At("TAIL"))
    private void onInitializeClientMixin(CallbackInfo ci) {
        ChunkDebugFromMixin.setChunkDebugClient(this);
    }

    public ChunkDebugMapImpl unlimited_Trade$getChunkDebugMap() {
        return (ChunkDebugMapImpl) map;
    }
}
