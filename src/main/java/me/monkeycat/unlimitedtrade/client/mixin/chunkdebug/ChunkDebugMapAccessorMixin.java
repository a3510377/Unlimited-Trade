package me.monkeycat.unlimitedtrade.client.mixin.chunkdebug;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugDataImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugMapImpl;
import me.monkeycat.unlimitedtrade.client.utils.ModIds;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;

@Restriction(require = @Condition(ModIds.chunkdebug))
@Pseudo
@Mixin(targets = "me.senseiwells.chunkdebug.client.gui.ChunkDebugMap")
public abstract class ChunkDebugMapAccessorMixin implements ChunkDebugMapImpl {
    @Final
    @Shadow(remap = false)
    private Map<RegistryKey<World>, ChunkDebugDataImpl> states;
    @Final
    @Shadow(remap = false)
    private List<RegistryKey<World>> dimensions;

    @Override
    public Map<RegistryKey<World>, ChunkDebugDataImpl> unlimited_Trade$getStates() {
        return states;
    }

    @Override
    public List<RegistryKey<World>> unlimited_Trade$getDimensions() {
        return dimensions;
    }
}
