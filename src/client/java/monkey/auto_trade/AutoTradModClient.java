package monkey.auto_trade;

import fi.dy.masa.itemscroller.util.InventoryUtils;
import fi.dy.masa.itemscroller.villager.VillagerDataStorage;
import fi.dy.masa.malilib.util.GuiUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import monkey.auto_trade.chunkdebug.ChunkdebugApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.ChunkPos;

import java.util.Objects;

import static net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.START_CLIENT_TICK;

public class AutoTradModClient implements ClientModInitializer {
    static ChunkdebugApi CHUNK_DEBUG = new ChunkdebugApi();
    static ChunkPos villagerOldPos;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ChunkdebugApi.PACKET_ID, (client, handler, buf, responseSender) -> {
            CHUNK_DEBUG.handlePacket(buf, handler);
        });

        START_CLIENT_TICK.register(client -> {
            if (client.crosshairTarget instanceof EntityHitResult hitResult) {
                Entity entity = hitResult.getEntity();
                if (entity instanceof VillagerEntity) {
                    villagerOldPos = entity.getChunkPos();

                    Screen screen = GuiUtils.getCurrentScreen();
                    if (screen instanceof MerchantScreen merchantScreen) {
                        MerchantScreenHandler handler = merchantScreen.getScreenHandler();
                        IntArrayList favorites = VillagerDataStorage.getInstance().getFavoritesForCurrentVillager(handler).favorites;

                        if (!Objects.requireNonNull(client.player).isSneaking() && !favorites.isEmpty()) {
                            InventoryUtils.villagerTradeEverythingPossibleWithAllFavoritedTrades();
                            merchantScreen.close();
                        }
                    }
                }
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("start").executes(context -> {
                context.getSource().sendFeedback(Text.literal("Hello, world!"));
                CHUNK_DEBUG.requestChunkData();

                return 0;
            }));
        });
    }
}