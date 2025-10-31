package net.xiaoyu.ride_casually.util;

import net.minecraft.world.entity.player.Player;
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
}