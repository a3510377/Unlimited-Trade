package me.monkeycat.unlimitedtrade.server.mixin;

import me.monkeycat.unlimitedtrade.server.UnlimitedTradeModServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MerchantEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "onRemove", at = @At("TAIL"))
    public void onRemove(Entity.RemovalReason reason, CallbackInfo ci) {
        if ((Object) this instanceof MerchantEntity merchantEntity) {
            UnlimitedTradeModServer unlimitedTradeModServer = UnlimitedTradeModServer.getInstance();

            if (unlimitedTradeModServer == null) return;

            unlimitedTradeModServer.sendNewStatus(merchantEntity);
            if (merchantEntity.getRemovalReason() == Entity.RemovalReason.KILLED) {
                unlimitedTradeModServer.removeMerchant(merchantEntity.getUuid());
            }
        }
    }
}
