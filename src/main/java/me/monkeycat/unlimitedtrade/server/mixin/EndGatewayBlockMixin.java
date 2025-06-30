package me.monkeycat.unlimitedtrade.server.mixin;

import net.minecraft.block.EndGatewayBlock;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(EndGatewayBlock.class)
public abstract class EndGatewayBlockMixin {
    @Inject(method = "createTeleportTarget", at = @At("HEAD"), cancellable = true)
    private void injectCreateTeleportTarget(ServerWorld world, Entity entity, BlockPos pos, CallbackInfoReturnable<TeleportTarget> cir) {
        if (world.getBlockEntity(pos) instanceof EndGatewayBlockEntity endGatewayBlockEntity) {
            Vec3d vec3d = endGatewayBlockEntity.getOrCreateExitPortalPos(world, pos);
            if (vec3d != null) {
                Set<PositionFlag> flags = PositionFlag.combine(PositionFlag.DELTA, PositionFlag.ROT);
                TeleportTarget.PostDimensionTransition ticketBehavior = TeleportTarget.ADD_PORTAL_CHUNK_TICKET;

                if (entity instanceof EnderPearlEntity) {
                    flags = Set.of();
                } else if (entity instanceof ServerPlayerEntity) {
                    ticketBehavior = TeleportTarget.NO_OP;
                }

                cir.setReturnValue(new TeleportTarget(world, vec3d, Vec3d.ZERO, 0.0F, 0.0F, flags, ticketBehavior));
                return;
            }
        }

        cir.setReturnValue(null);
    }
}
