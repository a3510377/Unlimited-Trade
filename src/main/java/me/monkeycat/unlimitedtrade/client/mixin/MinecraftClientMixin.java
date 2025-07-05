package me.monkeycat.unlimitedtrade.client.mixin;

import me.monkeycat.unlimitedtrade.client.UnlimitedTradeModClient;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    public void startWorldTick(CallbackInfo ci) {
        UnlimitedTradeModClient client = UnlimitedTradeModClient.getInstance();
        if (client != null) {
            client.tickHandle((MinecraftClient) (Object) this);
        }
    }
}
