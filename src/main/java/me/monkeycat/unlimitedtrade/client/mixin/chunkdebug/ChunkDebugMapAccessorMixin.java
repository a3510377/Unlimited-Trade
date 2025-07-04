package me.monkeycat.unlimitedtrade.client.mixin.chunkdebug;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugDataImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugDimensionStateImpl;
import me.monkeycat.unlimitedtrade.client.impl.ChunkDebugMapImpl;
import me.monkeycat.unlimitedtrade.common.utils.ModIds;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Restriction(require = @Condition(ModIds.chunkdebug))
@Pseudo
@Mixin(targets = "me.senseiwells.chunkdebug.client.gui.ChunkDebugMap")
public abstract class ChunkDebugMapAccessorMixin implements ChunkDebugMapImpl {
    @Final
    @Shadow(remap = false)
    private Map<RegistryKey<World>, Object> states;

    @Override
    public Map<RegistryKey<World>, ChunkDebugDimensionStateImpl> unlimited_Trade$getStates() {
        Map<RegistryKey<World>, ChunkDebugDimensionStateImpl> result = new HashMap<>();
        for (Map.Entry<RegistryKey<World>, Object> entry : this.states.entrySet()) {
            if (entry.getValue() instanceof ChunkDebugDimensionStateImpl impl) {
                result.put(entry.getKey(), impl);
            } else {
                throw new ClassCastException("Invalid state type: " + entry.getValue().getClass());
            }
        }
        return result;
    }
}
