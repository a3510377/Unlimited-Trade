package me.monkeycat.unlimitedtrade.server.mixin;

import me.monkeycat.unlimitedtrade.common.network.MerchantEntityStatusPayload;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "onRemove", at = @At("TAIL"))
    public void onRemove(Entity.RemovalReason reason, CallbackInfo ci) {
        if ((Object) this instanceof MerchantEntity merchantEntity) {
            @Nullable PlayerEntity playerEntity = merchantEntity.getCustomer();
            if (playerEntity == null) return;

            MerchantEntityStatusPayload payload = MerchantEntityStatusPayload.getFromMerchantEntity(merchantEntity);
        }
    }
}
