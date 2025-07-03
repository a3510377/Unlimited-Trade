package me.monkeycat.unlimitedtrade.server.mixin;

import me.monkeycat.unlimitedtrade.server.UnlimitedTradeModSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MerchantEntity.class)
public abstract class MerchantEntityMixin extends LivingEntity {
    protected MerchantEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract PlayerEntity getCustomer();

    @Inject(method = "canInteract", at = @At("HEAD"), cancellable = true)
    public void canInteract(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (!UnlimitedTradeModSettings.enablePersistentTradingScreen) return;

        RemovalReason reason = this.getRemovalReason();

        boolean allow = this.getCustomer() == player && this.getHealth() > 0.0F && (reason == null || switch (reason) {
            case UNLOADED_TO_CHUNK, UNLOADED_WITH_PLAYER, CHANGED_DIMENSION -> true;
            default -> false;
        });

        cir.setReturnValue(allow);
    }
}
