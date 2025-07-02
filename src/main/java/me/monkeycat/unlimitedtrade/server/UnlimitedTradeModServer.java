package me.monkeycat.unlimitedtrade.server;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import me.monkeycat.unlimitedtrade.common.network.MerchantEntityStatusPayload;
import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeHelloPayload;
import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeStartWatchPayload;
import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeStopWatchPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class UnlimitedTradeModServer implements ModInitializer {
    @Nullable
    private static UnlimitedTradeModServer instance = null;

    // Map: merchantUUID -> Set of playerUUIDs who are watching this merchant
    private final Multimap<UUID, UUID> watching = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    @Nullable
    public static UnlimitedTradeModServer getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        instance = this;

        ServerPlayConnectionEvents.JOIN.register(this::sendHelloPayload);
        ServerPlayConnectionEvents.DISCONNECT.register(this::handleDisconnect);

        ServerPlayNetworking.registerGlobalReceiver(UnlimitedTradeStartWatchPayload.ID, this::handleStartWatch);
        ServerPlayNetworking.registerGlobalReceiver(UnlimitedTradeStopWatchPayload.ID, this::handleStopWatch);
    }

    private void sendHelloPayload(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {
        packetSender.sendPacket(UnlimitedTradeHelloPayload.INSTANCE);
    }

    private void handleStartWatch(UnlimitedTradeStartWatchPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity serverPlayer = context.player();
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
        serverPlayer.networkHandler.sendPacket(new CustomPayloadS2CPacket(sendPayload));
    }

    private void handleStopWatch(UnlimitedTradeStopWatchPayload payload, ServerPlayNetworking.Context context) {
        synchronized (watching) {
            watching.remove(payload.uuid(), context.player().getUuid());
        }
    }

    private void handleDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
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
                serverPlayer.networkHandler.sendPacket(new CustomPayloadS2CPacket(payload));
            }
        }
    }

    public void removeMerchant(UUID merchantUuid) {
        synchronized (watching) {
            watching.removeAll(merchantUuid);
        }
    }
}
