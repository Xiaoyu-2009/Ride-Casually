package net.xiaoyu.ride_casually.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.xiaoyu.ride_casually.RideCasually;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;

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
                                sender.startRiding(targetEntity);
                                syncWithPlayers(sender, targetEntity);
                            }
                        } else {
                            Entity vehicle = sender.getVehicle();
                            sender.stopRiding();
                            syncWithPlayers(sender, vehicle);
                        }
                    }
                });
            }
        );
    }
    
    private static void syncWithPlayers(ServerPlayer player, Entity entity) {
        if (entity != null) {
            player.getServer().getPlayerList().broadcastAll(new ClientboundSetPassengersPacket(entity));
        }
        player.getServer().getPlayerList().broadcastAll(new ClientboundTeleportEntityPacket(player));
    }
}