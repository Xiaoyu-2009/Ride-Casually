package net.xiaoyu.ride_casually.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.xiaoyu.ride_casually.RideCasually;
import net.xiaoyu.ride_casually.RideConfig;
import net.xiaoyu.ride_casually.entity.BlockRideEntity;
import net.xiaoyu.ride_casually.event.BlockRideHandler;
import net.xiaoyu.ride_casually.util.BlockRideUtil;
import net.xiaoyu.ride_casually.util.RidePermissionUtil;
import net.xiaoyu.ride_casually.util.RideUtil;

@EventBusSubscriber(modid = RideCasually.MOD_ID)
public class NetworkHandler {
    
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(RideCasually.MOD_ID);
        
        registrar.playToServer(
            RidePacket.TYPE,
            RidePacket.STREAM_CODEC,
            (payload, context) -> {
                context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer sender) {
                        if (payload.isBlockRide()) {
                            if (RideConfig.ALLOW_BLOCK_RIDING.get()) {
                                BlockRideHandler.handleBlockRideRequest(sender, payload.blockPos());
                            }
                        } else {
                            if (RideConfig.ALLOW_ENTITY_RIDING.get()) {
                                if (payload.startRiding()) {
                                    Entity targetEntity = sender.level().getEntity(payload.entityId());

                                    if (targetEntity != null && RidePermissionUtil.isEntityRidingAllowed(targetEntity)) {
                                        RideUtil.addModRidingPlayer(sender);
                                        sender.startRiding(targetEntity);
                                        RideUtil.syncWithPlayers(sender, targetEntity);
                                    }
                                } else {
                                    Entity vehicle = sender.getVehicle();
                                    sender.stopRiding();

                                    if (vehicle instanceof BlockRideEntity) {
                                        vehicle.remove(Entity.RemovalReason.DISCARDED);
                                        BlockRideUtil.removeBlockRideEntity((BlockRideEntity) vehicle);
                                    }
                                    
                                    RideUtil.removeModRidingPlayer(sender);
                                    RideUtil.syncWithPlayers(sender, vehicle);
                                }
                            }
                        }
                    }
                });
            }
        );
    }
}