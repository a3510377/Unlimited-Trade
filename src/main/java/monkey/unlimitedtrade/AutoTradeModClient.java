package monkey.unlimitedtrade;

import fi.dy.masa.itemscroller.util.InventoryUtils;
import fi.dy.masa.itemscroller.villager.VillagerDataStorage;
import fi.dy.masa.malilib.util.GuiUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import monkey.unlimitedtrade.config.Configs;
import monkey.unlimitedtrade.config.types.AfterTradeActions;
import monkey.unlimitedtrade.utils.chunkdebug.BaseChunkData;
import monkey.unlimitedtrade.utils.chunkdebug.BaseChunkDebug;
import monkey.unlimitedtrade.utils.chunkdebug.ChunkDebugAPI;
import monkey.unlimitedtrade.utils.chunkdebug.essential.EssentialChunkDebugAPI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.item.Item;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.START_CLIENT_TICK;

public class AutoTradeModClient implements ClientModInitializer {
    public static final String MOD_ID = "unlimitedtrade";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static String VERSION = "unknown";
    public static BaseChunkDebug CHUNK_DEBUG_API;
    public static int remainingUseRetries = 0;
    @Nullable
    public static MerchantEntity tradeEntity;

    /**
     * try to interact block
     *
     * @param client Minecraft Client
     */
    public static void tryInteractBlock(MinecraftClient client) {
        if (Configs.afterTradeActions.getOptionListValue() == AfterTradeActions.USE ||
                Configs.afterTradeActions.getOptionListValue() == AfterTradeActions.USE_AND_DROP) {
            if (client.crosshairTarget instanceof BlockHitResult hitResult && client.interactionManager != null) {
                if (client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult) != ActionResult.PASS) {
                    remainingUseRetries--;
                    return;
                }
            }
        }
        remainingUseRetries = 0;
    }

    public static void startTrade(MinecraftClient client, MerchantScreen merchantScreen) {
        MerchantScreenHandler handler = merchantScreen.getScreenHandler();
        IntArrayList favorites = VillagerDataStorage.getInstance().getFavoritesForCurrentVillager(handler).favorites;

        if (favorites.isEmpty()) return;

        InventoryUtils.villagerTradeEverythingPossibleWithAllFavoritedTrades();

        // drop all sell item
        if (Configs.afterTradeActions.getOptionListValue() == AfterTradeActions.USE_AND_DROP) {
            for (int index = 0; index < favorites.size(); ++index) {
                Item sellItem = handler.getRecipes().get(index).getSellItem().getItem();

                if (Configs.dropBlockList.getStrings().contains(sellItem.getName().toString())) {
                    continue;
                }

                for (Slot slot : handler.slots) {
                    if (slot.getStack().getItem().equals(sellItem)) {
                        InventoryUtils.dropStack(merchantScreen, slot.id);
                    }
                }
            }
        }

        merchantScreen.close();

        remainingUseRetries = 20;
        tryInteractBlock(client);
    }

    @Override
    public void onInitializeClient() {
        VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();

        Configs.init();

        if (FabricLoader.getInstance().isModLoaded("essential-client")) {
            LOGGER.info("essential-client is install, use essential-client chunk debug");
            CHUNK_DEBUG_API = new EssentialChunkDebugAPI();
        } else {
            LOGGER.info("essential-client is not install, use current chunk debug");
            CHUNK_DEBUG_API = new ChunkDebugAPI();
        }

        START_CLIENT_TICK.register(client -> {
//            if (client.player != null) {
//                LOGGER.info(String.valueOf(CHUNK_DEBUG_API.getChunkData(client.player.getChunkPos())));
//
//                Identifier world = client.player.getWorld().getRegistryKey().getValue();
//                if (!world.equals(CHUNK_DEBUG_API.getCurrentWorld())) CHUNK_DEBUG_API.requestChunkData(world);
//            }

            if (!Configs.startTrade.getBooleanValue()) {
                AutoTradeModClient.remainingUseRetries = 0;
                return;
            }

            if (AutoTradeModClient.remainingUseRetries > 0) AutoTradeModClient.tryInteractBlock(client);
            else if (client.crosshairTarget instanceof EntityHitResult hitResult) {
                if (hitResult.getEntity() instanceof MerchantEntity merchantEntity) {
                    tradeEntity = merchantEntity;
                    Identifier world = tradeEntity.getWorld().getRegistryKey().getValue();

                    if (client.interactionManager != null && client.player != null && GuiUtils.getCurrentScreen() == null) {
                        if (client.player.getPos().isInRange(tradeEntity.getPos(), 2)) {
                            client.interactionManager.interactEntity(client.player, tradeEntity, Hand.MAIN_HAND);
                        }
                    }

                    // if in listening world != villageOldWorld re-listen
                    if (CHUNK_DEBUG_API != null && (CHUNK_DEBUG_API.getCurrentWorld() == null || !CHUNK_DEBUG_API.getCurrentWorld().equals(world))) {
                        CHUNK_DEBUG_API.requestChunkData(world);
                    }
                }
            } else if (tradeEntity != null && GuiUtils.getCurrentScreen() instanceof MerchantScreen merchantScreen) {
                if (client.player != null && client.player.isSneaking()) return;

                if (!Configs.waitChunkDebug.getBooleanValue() || CHUNK_DEBUG_API != null) {
                    @Nullable BaseChunkData chunkData = CHUNK_DEBUG_API.getChunkData(tradeEntity.getChunkPos());

                    LOGGER.info(String.valueOf(chunkData));
                    if (chunkData != null && chunkData.getLevelType() == ChunkLevelType.INACCESSIBLE) {
                        LOGGER.info("Start");
                        AutoTradeModClient.startTrade(client, merchantScreen);
                    }
                }
            }
        });
    }
}
