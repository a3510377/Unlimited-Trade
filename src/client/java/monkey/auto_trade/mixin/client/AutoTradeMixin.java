package monkey.auto_trade.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class AutoTradeMixin {
    @Inject(method = "setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"))
    private void run(Screen screen, CallbackInfo ci) {
        if (screen instanceof MerchantScreen) {
            MerchantScreen merchantScreen = (MerchantScreen) screen;
//            MerchantEntity merchantEntity = screen.getClass();
//            MerchantEntity merchantEntity = merchantScreen.();

            System.out.println("target");
        }
    }
}
