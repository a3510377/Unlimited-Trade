package monkey.unlimitedtrade;

import fi.dy.masa.itemscroller.util.InventoryUtils;
import fi.dy.masa.itemscroller.villager.VillagerDataStorage;
import fi.dy.masa.malilib.util.GuiUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import monkey.unlimitedtrade.config.Configs;
import monkey.unlimitedtrade.config.types.AfterTradeActions;
import monkey.unlimitedtrade.utils.chunkdebug.ChunkdebugApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.item.Item;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.hendrixshen.magiclib.malilib.impl.ConfigHandler;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;

import static net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.START_CLIENT_TICK;

public class AutoTradModClient implements ClientModInitializer {
    public static final String MOD_ID = "unlimitedtrade";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static String VERSION = "unknown";
    public static ChunkdebugApi CHUNK_DEBUG = new ChunkdebugApi();
    @Nullable
    public static MerchantEntity tradeEntity;

    @Override
    public void onInitializeClient() {
        VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();

        ConfigManager cm = ConfigManager.get(MOD_ID);
        cm.parseConfigClass(Configs.class);
        ConfigHandler configHandler = new ConfigHandler(MOD_ID, cm, Configs.VERSION);
        ConfigHandler.register(configHandler);
        Configs.init(cm);

        START_CLIENT_TICK.register(client -> {
            if (!Configs.startTrade) return;

            if (client.crosshairTarget instanceof EntityHitResult hitResult) {
                if (hitResult.getEntity() instanceof MerchantEntity merchantEntity) {
                    tradeEntity = merchantEntity;
                    Identifier world = tradeEntity.getWorld().getRegistryKey().getValue();

                    if (client.interactionManager != null && client.player != null && GuiUtils.getCurrentScreen() == null) {
                        if (client.player.getPos().isInRange(tradeEntity.getPos(), 2)) {
                            client.interactionManager.interactEntity(client.player, tradeEntity, Hand.MAIN_HAND);
                        }
                    }

                    // if in listening world != villageOldWorld re-listen
                    if (CHUNK_DEBUG.getCurrentWorld() == null || !CHUNK_DEBUG.getCurrentWorld().equals(world)) {
                        CHUNK_DEBUG.requestChunkData(world);
                    }
                }
            } else if (tradeEntity != null && GuiUtils.getCurrentScreen() instanceof MerchantScreen merchantScreen) {
                if (client.player == null || client.player.isSneaking()) return;
                if (!Configs.waitChunkDebug || CHUNK_DEBUG.worldChunks.get(tradeEntity.getChunkPos()).levelType() == ChunkLevelType.INACCESSIBLE) {
                    this.startTrade(client, merchantScreen);
                }
            }
        });
    }

    public void startTrade(MinecraftClient client, MerchantScreen merchantScreen) {
        MerchantScreenHandler handler = merchantScreen.getScreenHandler();
        IntArrayList favorites = VillagerDataStorage.getInstance().getFavoritesForCurrentVillager(handler).favorites;

        if (favorites.isEmpty()) return;

        InventoryUtils.villagerTradeEverythingPossibleWithAllFavoritedTrades();

        // drop all sell item
        if (Configs.afterTradeActions == AfterTradeActions.USE_AND_DROP) {
            for (int index = 0; index < favorites.size(); ++index) {
                Item sellItem = handler.getRecipes().get(index).getSellItem().getItem();

                LOGGER.info(sellItem.getName().toString());
                if (Configs.dropBlockList.contains(sellItem.getName().toString())) {
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

        // interact block
        if (Configs.afterTradeActions == AfterTradeActions.USE || Configs.afterTradeActions == AfterTradeActions.USE_AND_DROP) {
            if (client.crosshairTarget instanceof BlockHitResult hitResult && client.interactionManager != null) {
                client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
            }
        }
    }
}