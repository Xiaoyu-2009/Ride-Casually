package net.xiaoyu.ride_casually.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.xiaoyu.ride_casually.RideCasually;
import net.xiaoyu.ride_casually.util.RideManager;

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
                        if (payload.startRiding()) {
                            Entity targetEntity = sender.level().getEntity(payload.entityId());
                            if (targetEntity != null) {
                                RideManager.addModRidingPlayer(sender);
                                sender.startRiding(targetEntity);
                                RideManager.syncWithPlayers(sender, targetEntity);
                            }
                        } else {
                            Entity vehicle = sender.getVehicle();
                            sender.stopRiding();
                            RideManager.removeModRidingPlayer(sender);
                            RideManager.syncWithPlayers(sender, vehicle);
                        }
                    }
                });
            }
        );
    }
}