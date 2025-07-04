package me.monkeycat.unlimitedtrade.client;

import fi.dy.masa.itemscroller.util.InventoryUtils;
import fi.dy.masa.itemscroller.villager.VillagerDataStorage;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import me.monkeycat.unlimitedtrade.client.config.Configs;
import me.monkeycat.unlimitedtrade.client.config.types.AfterTradeActions;
import me.monkeycat.unlimitedtrade.client.config.types.WaitProtoTypes;
import me.monkeycat.unlimitedtrade.client.protocol.chunkdebug.BaseChunkDebugFrom;
import me.monkeycat.unlimitedtrade.client.protocol.chunkdebug.ChunkDebugFromMixin;
import me.monkeycat.unlimitedtrade.client.protocol.chunkdebug.ChunkDebugFromNetwork;
import me.monkeycat.unlimitedtrade.client.protocol.unlimitedtrade.UnlimitedTradeProtocol;
import me.monkeycat.unlimitedtrade.common.utils.ModIds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.START_CLIENT_TICK;

public class UnlimitedTradeModClient implements ClientModInitializer {
    private static final long TRADE_COOLDOWN_MS = 1000;
    private static UnlimitedTradeModClient instance;
    @Nullable
    private static MerchantEntity currentMerchantEntity;
    private BaseChunkDebugFrom chunkDataAPI;
    private UnlimitedTradeProtocol unlimitedTradeProtocol;
    private long lastTradeCloseTime = 0;
    private boolean manuallyClosedTrade = false;
    private boolean hasOpenedScreen = false;

    public static UnlimitedTradeModClient getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        instance = this;
        unlimitedTradeProtocol = new UnlimitedTradeProtocol();

        Configs.init();
        unlimitedTradeProtocol.init();

        if (FabricLoader.getInstance().isModLoaded(ModIds.chunkdebug)) {
            chunkDataAPI = new ChunkDebugFromMixin();
        } else chunkDataAPI = new ChunkDebugFromNetwork();

        START_CLIENT_TICK.register(this::tickHandle);
    }

    public void tickHandle(MinecraftClient client) {
        if (!Configs.START_TRADE.getBooleanValue()) return;
        if (client.interactionManager == null || client.player == null) return;

        HitResult hitResult = client.crosshairTarget;
        long now = System.currentTimeMillis();

        // Skip if the trade screen was manually closed and cooldown is not over
        if (manuallyClosedTrade) {
            if (now - lastTradeCloseTime < TRADE_COOLDOWN_MS) return;
            manuallyClosedTrade = false; // Reset flag after cooldown
        }

        // Handle when the current screen is a merchant trading screen
        if (client.currentScreen instanceof MerchantScreen merchantScreen) {
            if (currentMerchantEntity == null) return;
            hasOpenedScreen = true;

            boolean shouldTrade = false;
            WaitProtoTypes waitProtoType = (WaitProtoTypes) Configs.WAIT_PROTO_TYPE.getOptionListValue();
            boolean checkChunkState = waitProtoType == WaitProtoTypes.AUTO || waitProtoType == WaitProtoTypes.CHUNKDEBUG;
            if (checkChunkState && !Boolean.FALSE.equals(chunkDataAPI.getEnabled())) {
                shouldTrade = chunkDataAPI.canTrade(currentMerchantEntity);
            }

            // Perform the trade and reset tracking flag
            if (shouldTrade) {
                startTrade(client, merchantScreen);
                merchantScreen.close();
                hasOpenedScreen = false;
            }
            return;
        }

        // If the trade screen was open and now closed without trading, trigger cooldown
        if (hasOpenedScreen) {
            lastTradeCloseTime = now;
            hasOpenedScreen = false;
            manuallyClosedTrade = true;
            return;
        }

        // Try to interact with a merchant entity to open trade screen
        if (client.crosshairTarget instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof MerchantEntity merchantEntity) {
            if (client.currentScreen != null || client.player.isSneaking()) return;

            currentMerchantEntity = merchantEntity;
            chunkDataAPI.startWatching(merchantEntity);

            if (!client.player.getPos().isInRange(merchantEntity.getPos(), 3)) return;

            chunkDataAPI.setStatusChangeFlag(false);
            client.interactionManager.interactEntity(client.player, merchantEntity, Hand.MAIN_HAND);
            return;
        }

        // Interact with target block if it's not in the block drop blacklist
        AfterTradeActions afterTradeActions = (AfterTradeActions) Configs.AFTER_TRADE_ACTIONS.getOptionListValue();
        if (afterTradeActions == AfterTradeActions.USE || afterTradeActions == AfterTradeActions.USE_AND_DROP) {
            if (hitResult instanceof BlockHitResult blockHitResult) {
                World world = client.world;
                if (world == null || client.player.isSneaking()) return;

                BlockPos pos = blockHitResult.getBlockPos();
                BlockState state = world.getBlockState(pos);
                Identifier id = Registries.BLOCK.getId(state.getBlock());

                boolean isTargetBlock = Configs.AFTER_USE_WHITE_LIST.getStrings().stream().anyMatch(target -> target.equals(id.toString()));
                if (!isTargetBlock) return;

                // Prevent putting it down
                ItemStack stackInHand = client.player.getMainHandStack();
                if (!stackInHand.isEmpty() && stackInHand.getItem() instanceof BlockItem) {
                    return;
                }

                client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, blockHitResult);
            }
        }

        if (client.currentScreen != null && !hasOpenedScreen) {
            hasOpenedScreen = true;
        }
    }

    public void startTrade(MinecraftClient client, MerchantScreen merchantScreen) {
        MerchantScreenHandler handler = merchantScreen.getScreenHandler();
        IntArrayList favorites = VillagerDataStorage.getInstance().getFavoritesForCurrentVillager(handler).favorites;

        if (favorites.isEmpty()) return;

        InventoryUtils.villagerTradeEverythingPossibleWithAllFavoritedTrades();

        // drop all sell item
        AfterTradeActions afterTradeActions = (AfterTradeActions) Configs.AFTER_TRADE_ACTIONS.getOptionListValue();
        if (afterTradeActions == AfterTradeActions.DROP || afterTradeActions == AfterTradeActions.USE_AND_DROP) {
            Set<Item> sellItems = new HashSet<>();
            List<TradeOffer> recipes = handler.getRecipes();

            for (int i = 0; i < favorites.size(); i++) {
                sellItems.add(recipes.get(i).getSellItem().getItem());
            }

            Set<String> dropBlockList = new HashSet<>(Configs.DROP_BLOCK_LIST.getStrings());
            for (Slot slot : handler.slots) {
                Item itemInSlot = slot.getStack().getItem();
                Identifier id = Registries.ITEM.getId(itemInSlot);

                if (dropBlockList.contains(id.toString())) continue;
                if (sellItems.contains(itemInSlot)) {
                    InventoryUtils.dropStack(merchantScreen, slot.id);
                }
            }
        }
    }

    public BaseChunkDebugFrom getChunkDataAPI() {
        return chunkDataAPI;
    }

    public UnlimitedTradeProtocol getUnlimitedTradeProtocol() {
        return unlimitedTradeProtocol;
    }
}
