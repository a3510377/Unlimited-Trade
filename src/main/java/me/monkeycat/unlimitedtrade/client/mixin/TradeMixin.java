package me.monkeycat.unlimitedtrade.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class TradeMixin {
    @Inject(method = "setScreen", at = @At("HEAD"))
    public void setScreen(Screen screen, CallbackInfo ci) {
        if (screen instanceof MerchantScreen) {
        }
    }
}
