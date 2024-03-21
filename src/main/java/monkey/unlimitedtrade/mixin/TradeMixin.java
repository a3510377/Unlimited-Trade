package monkey.unlimitedtrade.mixin;

import monkey.unlimitedtrade.AutoTradeModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static monkey.unlimitedtrade.AutoTradeModClient.CHUNK_DEBUG_API;

@Mixin(MinecraftClient.class)
public class TradeMixin {
    @Inject(method = "setScreen", at = @At("HEAD"))
    public void setScreen(Screen screen, CallbackInfo ci) {
        if (!(screen instanceof MerchantScreen) && CHUNK_DEBUG_API != null) {
            AutoTradeModClient.tradeEntity = null;
            CHUNK_DEBUG_API.requestChunkData();
        }
    }
}
