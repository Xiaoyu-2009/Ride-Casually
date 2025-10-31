package net.xiaoyu.ride_casually.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RideManager {
    private static final Set<UUID> modRidingPlayers = new HashSet<>();

    public static void addModRidingPlayer(Player player) {
        modRidingPlayers.add(player.getUUID());
    }

    public static void removeModRidingPlayer(Player player) {
        modRidingPlayers.remove(player.getUUID());
    }

    public static boolean isModRidingPlayer(Player player) {
        return modRidingPlayers.contains(player.getUUID());
    }
    
    public static void syncWithPlayers(ServerPlayer player, Entity entity) {
        if (entity != null) {
            player.getServer().getPlayerList().broadcastAll(new ClientboundSetPassengersPacket(entity));
        }
        player.getServer().getPlayerList().broadcastAll(new ClientboundTeleportEntityPacket(player));
    }
}