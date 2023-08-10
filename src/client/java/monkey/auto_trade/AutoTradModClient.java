package monkey.auto_trade;

import fi.dy.masa.malilib.util.GuiUtils;
import monkey.auto_trade.chunkdebug.ChunkdebugApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

import static net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.START_CLIENT_TICK;

public class AutoTradModClient implements ClientModInitializer {
    public static ChunkdebugApi CHUNK_DEBUG = new ChunkdebugApi();
    public static Vec3d villagerOldPos;
    public static Identifier villageOldWorld;
    public static boolean illimitedTradeToggle;
    private static int delayTick;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ChunkdebugApi.PACKET_ID, (client, handler, buf, responseSender) -> {
            CHUNK_DEBUG.handlePacket(buf, handler);
        });

        START_CLIENT_TICK.register(client -> {
            if (delayTick > 0) {
                delayTick--;
                return;
            }

            if (illimitedTradeToggle && client.crosshairTarget instanceof EntityHitResult hitResult) {
                Entity entity = hitResult.getEntity();
                if (entity instanceof VillagerEntity) {
                    villagerOldPos = entity.getPos();
                    villageOldWorld = entity.getWorld().getRegistryKey().getValue();

                    if (client.interactionManager != null && client.player != null && GuiUtils.getCurrentScreen() == null) {
                        if (client.player.getPos().isInRange(villagerOldPos, 2)) {
                            client.interactionManager.interactEntity(client.player, entity, Hand.MAIN_HAND);
                            delayTick = 20; // ~= 1s
                        }
                    }

                    // if in listening world != villageOldWorld re-listen
                    if (CHUNK_DEBUG.listen != null && !CHUNK_DEBUG.listen.equals(villageOldWorld)) {
                        CHUNK_DEBUG.requestChunkData(villageOldWorld);
                    }
                }
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("illimited_trade_toggle").executes(context -> {
                delayTick = 0;
                illimitedTradeToggle = !illimitedTradeToggle;
                context.getSource().sendFeedback(Text.literal(illimitedTradeToggle ? "自動交易啟動" : "自動交易關閉"));

                // if close remove listen
                if (!illimitedTradeToggle && CHUNK_DEBUG.listen != null) {
                    CHUNK_DEBUG.requestChunkData();
                }

                return 0;
            }));
        });
    }
}