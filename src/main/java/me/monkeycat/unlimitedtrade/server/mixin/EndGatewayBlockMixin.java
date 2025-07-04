package me.monkeycat.unlimitedtrade.server.mixin;

import me.monkeycat.unlimitedtrade.server.UnlimitedTradeModSettings;
import net.minecraft.block.EndGatewayBlock;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC > 12101
//$$ import net.minecraft.network.packet.s2c.play.PositionFlag;
//$$
//$$ import java.util.Set;
//#endif

@Mixin(EndGatewayBlock.class)
public abstract class EndGatewayBlockMixin {
    @Inject(method = "createTeleportTarget", at = @At("HEAD"), cancellable = true)
    private void injectCreateTeleportTarget(ServerWorld world, Entity entity, BlockPos pos, CallbackInfoReturnable<TeleportTarget> cir) {
        if (!(world.getBlockEntity(pos) instanceof EndGatewayBlockEntity endGatewayBlockEntity)) {
            cir.setReturnValue(null);
            return;
        }

        Vec3d vec3d = endGatewayBlockEntity.getOrCreateExitPortalPos(world, pos);
        if (vec3d == null) {
            cir.setReturnValue(null);
            return;
        }

        TeleportTarget.PostDimensionTransition ticketBehavior = TeleportTarget.ADD_PORTAL_CHUNK_TICKET;
        if (UnlimitedTradeModSettings.disableEndGatewayAnyTicket || (entity instanceof ServerPlayerEntity && UnlimitedTradeModSettings.disableEndGatewayPlayerTicket)) {
            ticketBehavior = TeleportTarget.NO_OP;
        }

        //#if MC > 12101
        //$$ Set<PositionFlag> flags = PositionFlag.combine(PositionFlag.DELTA, PositionFlag.ROT);
        //$$
        //$$ if (entity instanceof EnderPearlEntity) {
        //$$     flags = Set.of();
        //$$ }
        //$$
        //$$ cir.setReturnValue(new TeleportTarget(world, vec3d, Vec3d.ZERO, 0.0F, 0.0F, flags, ticketBehavior));
        //#else
        Vec3d velocity = entity instanceof EnderPearlEntity ? new Vec3d(0.0F, -1.0F, 0.0F) : entity.getVelocity();

        cir.setReturnValue(new TeleportTarget(world, vec3d, velocity, entity.getYaw(), entity.getPitch(), ticketBehavior));
        //#endif
    }
}
