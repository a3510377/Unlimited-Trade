package monkey.auto_trade.mixin.client;

import fi.dy.masa.itemscroller.util.InventoryUtils;
import fi.dy.masa.itemscroller.villager.VillagerDataStorage;
import fi.dy.masa.malilib.util.GuiUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import monkey.auto_trade.AutoTradModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(MinecraftClient.class)
public class AutoTradeMixin {
    @Shadow
    @Final
    private static Logger LOGGER;
    @Unique
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"))
    private void run(Screen screen, CallbackInfo ci) {
        if (AutoTradModClient.illimitedTradeToggle && GuiUtils.getCurrentScreen() instanceof MerchantScreen merchantScreen) {
            MerchantScreenHandler handler = merchantScreen.getScreenHandler();
            IntArrayList favorites = VillagerDataStorage.getInstance().getFavoritesForCurrentVillager(handler).favorites;

            if (!Objects.requireNonNull(player).isSneaking() && !favorites.isEmpty()) {
                Item sellItem = handler.getRecipes().get(0).getSellItem().getItem();

                for (Slot slot : handler.slots) {
                    if (slot.getStack().getItem().equals(sellItem)) {
                        InventoryUtils.dropStack(merchantScreen, slot.getIndex());
                    }
                }
                //                merchantScreen.close();
            }
        }
    }
}
