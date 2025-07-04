package me.monkeycat.unlimitedtrade.server.mixin;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.monkeycat.unlimitedtrade.common.utils.ModIds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 12104
//$$ import me.monkeycat.unlimitedtrade.server.UnlimitedTradeModSettings;
//$$ import net.minecraft.entity.player.PlayerEntity;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.21.4"))
@Mixin(MerchantEntity.class)
public abstract class MerchantEntityMixin extends LivingEntity {
    protected MerchantEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    //#if MC >= 12104
    //$$ @Shadow
    //$$ public abstract PlayerEntity getCustomer();
    //$$
    //$$ @Inject(method = "canInteract", at = @At("HEAD"), cancellable = true)
    //$$ public void canInteract(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
    //$$ if (!UnlimitedTradeModSettings.enablePersistentTradingScreen) return;
    //$$
    //$$ RemovalReason reason = this.getRemovalReason();
    //$$
    //$$ boolean allow = this.getCustomer() == player && this.getHealth() > 0.0F && (reason == null || switch (reason) {
    //$$ case UNLOADED_TO_CHUNK, UNLOADED_WITH_PLAYER, CHANGED_DIMENSION -> true;
    //$$ default -> false;
    //$$ });
    //$$
    //$$ cir.setReturnValue(allow);
    //$$ }
    //#endif
}
