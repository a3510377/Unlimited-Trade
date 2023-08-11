package monkey.auto_trade;

import fi.dy.masa.itemscroller.util.InventoryUtils;
import fi.dy.masa.itemscroller.villager.VillagerDataStorage;
import fi.dy.masa.malilib.util.GuiUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import monkey.auto_trade.chunkdebug.ChunkData;
import monkey.auto_trade.chunkdebug.ChunkdebugApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.Item;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;

import static monkey.auto_trade.AutoTradMod.LOGGER;
import static net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.START_CLIENT_TICK;

public class AutoTradModClient implements ClientModInitializer {
    public static ChunkdebugApi CHUNK_DEBUG = new ChunkdebugApi();
    public static Entity tradeEntity;
    public static boolean illimitedTradeToggle;
    public static boolean startTrade;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ChunkdebugApi.PACKET_ID, (client, handler, buf, responseSender) -> {
            CHUNK_DEBUG.handlePacket(buf, handler);
        });

        START_CLIENT_TICK.register(client -> {
            if (!illimitedTradeToggle) return;

            Screen screen = GuiUtils.getCurrentScreen();
            if (!startTrade) CHUNK_DEBUG.requestChunkData();
            else if (screen instanceof MerchantScreen merchantScreen) {
                MerchantScreenHandler handler = merchantScreen.getScreenHandler();
                IntArrayList favorites = VillagerDataStorage.getInstance().getFavoritesForCurrentVillager(handler).favorites;

                if (client.player != null && !client.player.isSneaking() && !favorites.isEmpty()) {
                    Identifier world = tradeEntity.getWorld().getRegistryKey().getValue();
                    ChunkData data = ChunkData.serverWorldChunks.get(world).get(tradeEntity.getChunkPos());
                    LOGGER.info(String.valueOf(data.levelType()));
                    if (data.levelType() == ChunkLevelType.INACCESSIBLE) {
                        InventoryUtils.villagerTradeEverythingPossibleWithAllFavoritedTrades();

                        for (int index = 0; index < favorites.size(); ++index) {
                            Item sellItem = handler.getRecipes().get(index).getSellItem().getItem();

                            // drop all sell item
                            for (Slot slot : handler.slots) {
                                if (slot.getStack().getItem().equals(sellItem)) {
                                    InventoryUtils.dropStack(merchantScreen, slot.id);
                                }
                            }
                        }
                        merchantScreen.close();
                    }
                }
            }

            if (client.crosshairTarget instanceof EntityHitResult hitResult) {
                Entity entity = hitResult.getEntity();
                if (entity instanceof VillagerEntity || entity instanceof WanderingTraderEntity) {
                    tradeEntity = entity;
                    Identifier world = entity.getWorld().getRegistryKey().getValue();

                    if (client.interactionManager != null && client.player != null && screen == null) {
                        if (client.player.getPos().isInRange(tradeEntity.getPos(), 2)) {
                            client.interactionManager.interactEntity(client.player, entity, Hand.MAIN_HAND);
                        }
                    }

                    // if in listening world != villageOldWorld re-listen
                    if (CHUNK_DEBUG.listen == null || !CHUNK_DEBUG.listen.equals(world)) {
                        CHUNK_DEBUG.requestChunkData(world);
                    }
                }
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("illimited_trade_toggle").executes(context -> {
                illimitedTradeToggle = !illimitedTradeToggle;
                context.getSource().sendFeedback(Text.literal(illimitedTradeToggle ? "自動交易啟動" : "自動交易關閉"));

                if (!illimitedTradeToggle && CHUNK_DEBUG.listen != null) CHUNK_DEBUG.requestChunkData();

                return 0;
            }));
        });
    }
}