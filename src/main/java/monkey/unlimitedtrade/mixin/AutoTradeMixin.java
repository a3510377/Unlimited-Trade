package monkey.unlimitedtrade.mixin;

import monkey.unlimitedtrade.AutoTradModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static monkey.unlimitedtrade.AutoTradModClient.CHUNK_DEBUG;

@Mixin(MinecraftClient.class)
public class AutoTradeMixin {
    @Inject(method = "setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"))
    private void run(Screen screen, CallbackInfo ci) {
        if (!(screen instanceof MerchantScreen)) {
            AutoTradModClient.tradeEntity = null;
            CHUNK_DEBUG.requestChunkData();
        }
    }
}
