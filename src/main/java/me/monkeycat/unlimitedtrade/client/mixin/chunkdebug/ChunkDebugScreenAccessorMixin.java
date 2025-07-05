package me.monkeycat.unlimitedtrade.client.mixin.chunkdebug;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugBaseGetStatusImpl;
import me.monkeycat.unlimitedtrade.common.utils.ModIds;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Restriction(require = @Condition(value = ModIds.chunkdebug, versionPredicates = ">=1.0.0 <2.0.0"))
@Pseudo
@Mixin(targets = "me.senseiwells.chunkdebug.client.gui.ChunkDebugScreen")
public abstract class ChunkDebugScreenAccessorMixin implements ChunkDebugBaseGetStatusImpl {
    @Final
    @Shadow(remap = false)
    private Map<RegistryKey<World>, Object> states;

    @Override
    public Map<RegistryKey<World>, Object> unlimited_Trade$getStates() {
        return this.states;
    }
}
