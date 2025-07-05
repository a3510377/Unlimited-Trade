package me.monkeycat.unlimitedtrade.server;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import me.fallenbreath.fanetlib.api.event.FanetlibServerEvents;
import me.fallenbreath.fanetlib.api.packet.FanetlibPackets;
import me.fallenbreath.fanetlib.api.packet.PacketHandlerC2S;
import me.monkeycat.unlimitedtrade.common.network.MerchantEntityStatusPayload;
import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeHelloPayload;
import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeStartWatchPayload;
import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeStopWatchPayload;
import me.monkeycat.unlimitedtrade.server.utils.UnlimitedTradeTranslations;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class UnlimitedTradeModServer implements CarpetExtension, ModInitializer {
    private static final AtomicBoolean registeredPackers = new AtomicBoolean();
    // Map: merchantUUID -> Set of playerUUIDs who are watching this merchant
    private static final Multimap<UUID, UUID> watching = Multimaps.synchronizedSetMultimap(HashMultimap.create());
    @Nullable
    private static UnlimitedTradeModServer instance = null;

    @Nullable
    public static UnlimitedTradeModServer getInstance() {
        return instance;
    }

    public static void registerPacketHandlers() {
        if (!registeredPackers.compareAndSet(false, true)) {
            return;
        }

        FanetlibPackets.registerC2S(UnlimitedTradeStartWatchPayload.ID, UnlimitedTradeStartWatchPayload.PACKET_CODEC, UnlimitedTradeModServer::handleStartWatch);
        FanetlibPackets.registerC2S(UnlimitedTradeStopWatchPayload.ID, UnlimitedTradeStopWatchPayload.PACKET_CODEC, UnlimitedTradeModServer::handleStopWatch);
    }

    private static void handleStartWatch(UnlimitedTradeStartWatchPayload payload, PacketHandlerC2S.Context context) {
        if (!UnlimitedTradeModSettings.enableUnlimitedTradeProtocol) return;

        ServerPlayerEntity serverPlayer = context.getPlayer();
        MinecraftServer server = serverPlayer.getServer();
        if (server == null) return;

        ServerWorld serverWorld = server.getWorld(payload.world());
        if (serverWorld == null) return;

        Entity entity = serverWorld.getEntity(payload.uuid());
        if (!(entity instanceof MerchantEntity merchant)) return;

        UUID playerUuid = serverPlayer.getUuid();
        synchronized (watching) {
            if (!watching.containsEntry(merchant.getUuid(), playerUuid)) {
                watching.put(merchant.getUuid(), playerUuid);
            }
        }

        MerchantEntityStatusPayload sendPayload = MerchantEntityStatusPayload.getFromMerchantEntity(merchant);
        System.out.println("Sending new status to player " + serverPlayer.getName().getString() + " for merchant " + merchant.getUuid());
        serverPlayer.networkHandler.sendPacket(FanetlibPackets.createS2C(sendPayload.getId(), sendPayload));
    }

    private static void handleStopWatch(UnlimitedTradeStopWatchPayload payload, PacketHandlerC2S.Context context) {
        synchronized (watching) {
            watching.remove(payload.uuid(), context.getPlayer().getUuid());
        }
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(UnlimitedTradeModSettings.class);
    }

    @Override
    public void onInitialize() {
        instance = this;
        CarpetServer.manageExtension(new UnlimitedTradeModServer());

        FanetlibServerEvents.registerPlayerJoinListener(this::sendHelloPayload);
        FanetlibServerEvents.registerPlayerDisconnectListener(this::handleDisconnect);
    }

    private void sendHelloPayload(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player) {
        System.out.println("Sending Hello to player: " + player.getName().getString());
        player.networkHandler.sendPacket(FanetlibPackets.createS2C(UnlimitedTradeHelloPayload.ID, UnlimitedTradeHelloPayload.INSTANCE));
    }

    private void handleDisconnect(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player) {
        UUID playerUuid = handler.player.getUuid();
        synchronized (watching) {
            watching.values().removeIf(uuid -> uuid.equals(playerUuid));
        }
    }

    public void sendNewStatus(MerchantEntity merchantEntity) {
        MinecraftServer server = merchantEntity.getServer();
        if (server == null) {
            return;
        }

        UUID merchantUuid = merchantEntity.getUuid();
        Collection<UUID> playerUuids;
        synchronized (watching) {
            if (!watching.containsKey(merchantUuid)) {
                return;
            }

            playerUuids = new ArrayList<>(watching.get(merchantUuid));
        }

        PlayerManager playerManager = server.getPlayerManager();
        MerchantEntityStatusPayload payload = MerchantEntityStatusPayload.getFromMerchantEntity(merchantEntity);
        for (UUID playerUuid : playerUuids) {
            ServerPlayerEntity serverPlayer = playerManager.getPlayer(playerUuid);

            if (serverPlayer != null) {
                serverPlayer.networkHandler.sendPacket(FanetlibPackets.createS2C(payload.getId(), payload));
            }
        }
    }

    public void removeMerchant(UUID merchantUuid) {
        synchronized (watching) {
            watching.removeAll(merchantUuid);
        }
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return UnlimitedTradeTranslations.getTranslationFromResourcePath(lang);
    }
}
