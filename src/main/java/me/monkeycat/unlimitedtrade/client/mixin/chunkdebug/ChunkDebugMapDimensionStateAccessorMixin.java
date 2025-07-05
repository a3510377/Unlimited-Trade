package me.monkeycat.unlimitedtrade.client.mixin.chunkdebug;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugDimensionStateImpl;
import me.monkeycat.unlimitedtrade.common.utils.ModIds;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

@Restriction(require = @Condition(value = ModIds.chunkdebug, versionPredicates = ">2.0.0"))
@Pseudo
@Mixin(targets = "me.senseiwells.chunkdebug.client.gui.ChunkDebugMap$DimensionState")
public class ChunkDebugMapDimensionStateAccessorMixin implements ChunkDebugDimensionStateImpl {
    @Final
    @Shadow(remap = false)
    private Long2ObjectMap<Object> chunks;

    @Override
    public Long2ObjectMap<Object> unlimited_Trade$getChunks() {
        return chunks;
    }
}
