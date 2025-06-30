package me.monkeycat.unlimitedtrade.client.mixin.chunkdebug;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugDataImpl;
import me.monkeycat.unlimitedtrade.client.utils.ModIds;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Restriction(require = @Condition(ModIds.chunkdebug))
@Pseudo
@Mixin(targets = "me.senseiwells.chunkdebug.common.utils.ChunkData")
public abstract class ChunkDebugDataMixin implements ChunkDebugDataImpl {
    @Override
    public ChunkPos unlimited_Trade$position() {
        return null;
    }

    @Override
    public @Nullable ChunkStatus unlimited_Trade$stage() {
        return null;
    }
}
