package me.monkeycat.unlimitedtrade.server;

import me.monkeycat.unlimitedtrade.common.network.UnlimitedTradeHelloPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class UnlimitedTradeModServer implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.JOIN.register(this::sendHelloPayload);
    }

    private void sendHelloPayload(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {
        packetSender.sendPacket(UnlimitedTradeHelloPayload.INSTANCE);
    }
}
