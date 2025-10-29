package net.xiaoyu.ride_casually.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.xiaoyu.ride_casually.RideCasually;

@EventBusSubscriber(modid = RideCasually.MOD_ID)
public class NetworkHandler {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(RideCasually.MOD_ID);
        
        registrar.playToServer(
            RideEntityPacket.TYPE,
            RideEntityPacket.STREAM_CODEC,
            (payload, context) -> {
                context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer sender) {
                        if (sender.level().getEntity(payload.entityId()) != null) {
                            sender.startRiding(sender.level().getEntity(payload.entityId()));
                        }
                    }
                });
            }
        );
    }
}