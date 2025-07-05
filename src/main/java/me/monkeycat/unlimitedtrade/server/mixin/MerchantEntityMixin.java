package me.monkeycat.unlimitedtrade.server.mixin;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.monkeycat.unlimitedtrade.common.utils.ModIds;
import me.monkeycat.unlimitedtrade.common.utils.mixin.DummyClass;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.21.4"))
@Mixin(DummyClass.class)
public class MerchantEntityMixin {
}
