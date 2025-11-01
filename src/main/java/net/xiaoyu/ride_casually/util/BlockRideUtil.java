package net.xiaoyu.ride_casually.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.resources.ResourceLocation;
import net.xiaoyu.ride_casually.data.BlockOffsetManager;
import net.xiaoyu.ride_casually.entity.BlockRideEntity;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class BlockRideUtil {
    private static final Map<ResourceLocation, Map<BlockPos, Pair<BlockRideEntity, Vec3>>> OCCUPIED = new HashMap<>();
    
    public static class Offset {
        public final double x;
        public final double y;
        public final double z;

        public Offset(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static Offset getOffset(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        Offset dataOffset = BlockOffsetManager.getInstance().getOffsetForBlock(state);
        if (dataOffset != null) {
            return dataOffset;
        }

        return new Offset(0.5D, 1.0D, 0.5D);
    }

    public static Vec3 getDismountLocationForPassenger(Player player, BlockRideEntity blockRideEntity) {
        Vec3 playerPos = player.position();
        return new Vec3(playerPos.x, playerPos.y + 1.0D, playerPos.z);
            
/*        Vec3 resetPosition = getPreviousPlayerPosition(player, blockRideEntity);

        if (resetPosition != null) {
            BlockPos belowResetPos = BlockPos.containing(resetPosition.x, resetPosition.y - 1, resetPosition.z);

            if (!player.level().getBlockState(belowResetPos).isFaceSturdy(player.level(), belowResetPos, Direction.UP, SupportType.FULL))
                return new Vec3(resetPosition.x, resetPosition.y + 1, resetPosition.z);
            else
                return resetPosition;
        }

        return null;*/
    }

    public static boolean isPlayerInRange(Player player, BlockPos pos) {
/*         BlockPos playerPos = player.blockPosition();
        double blockReachDistance = 2;

        if (blockReachDistance == 0) 
        return playerPos.getY() - pos.getY() <= 1 &&
            playerPos.getX() - pos.getX() == 0 &&
            playerPos.getZ() - pos.getZ() == 0;

        pos = BlockPos.containing(
            pos.getX() + 0.5D, 
            pos.getY() + 0.5D, 
            pos.getZ() + 0.5D
        );

        AABB range = new AABB(
            pos.getX() + blockReachDistance, 
            pos.getY() + blockReachDistance, 
            pos.getZ() + blockReachDistance, 
            pos.getX() - blockReachDistance, 
            pos.getY() - blockReachDistance, 
            pos.getZ() - blockReachDistance
        );

        playerPos = BlockPos.containing(
            playerPos.getX() + 0.5D, 
            playerPos.getY() + 0.5D, 
            playerPos.getZ() + 0.5D
        );
        return range.minX <= playerPos.getX() &&
            range.minY <= playerPos.getY() &&
            range.minZ <= playerPos.getZ() &&
            range.maxX >= playerPos.getX() &&
            range.maxY >= playerPos.getY() &&
            range.maxZ >= playerPos.getZ(); */

        return true;
    }

    public static void handleBlockRideRequest(Player player, BlockPos pos) {
        Level level = player.level();
        
        if (!level.isClientSide) {
            if ((!isPlayerSitting(player) || player.getVehicle() instanceof BlockRideEntity) && !player.isShiftKeyDown()) {
                if (isPlayerInRange(player, pos) && !isOccupied(level, pos) && level.getBlockState(pos.above()).isAir()) {
                    if (player.getVehicle() instanceof BlockRideEntity currentRideEntity) {
                        player.stopRiding();
                        currentRideEntity.ejectPassengers();
                        currentRideEntity.remove(Entity.RemovalReason.DISCARDED);
                    }
                    
                    BlockRideEntity rideEntity = new BlockRideEntity(level, pos);

                    if (addBlockRideEntity(level, pos, rideEntity, player.position())) {
                        level.addFreshEntity(rideEntity);
                        player.startRiding(rideEntity);
                    }
                }
            }
        }
    }
    
    public static boolean addBlockRideEntity(Level level, BlockPos blockPos, BlockRideEntity entity, Vec3 playerPos) {
        if (!level.isClientSide) {
            ResourceLocation id = getDimensionTypeId(level);

            if (!OCCUPIED.containsKey(id)) OCCUPIED.put(id, new HashMap<>());

            OCCUPIED.get(id).put(blockPos, Pair.of(entity, playerPos));
            return true;
        }

        return false;
    }

    public static void removeBlockRideEntity(BlockRideEntity blockRideEntity) {
        removeBlockRideEntity(blockRideEntity.level(), blockRideEntity.blockPosition());
    }

    public static boolean removeBlockRideEntity(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            ResourceLocation id = getDimensionTypeId(level);

            if (OCCUPIED.containsKey(id)) {
                OCCUPIED.get(id).remove(pos);
                return true;
            }
        }

        return false;
    }

    public static BlockRideEntity getBlockRideEntity(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            ResourceLocation id = getDimensionTypeId(level);

            if (OCCUPIED.containsKey(id) && OCCUPIED.get(id).containsKey(pos)) return OCCUPIED.get(id).get(pos).getLeft();
        }

        return null;
    }

/*    public static Vec3 getPreviousPlayerPosition(Player player, BlockRideEntity blockRideEntity) {
        if (!player.level().isClientSide) {
            ResourceLocation id = getDimensionTypeId(player.level());

            if (OCCUPIED.containsKey(id)) {
                for (Pair<BlockRideEntity, Vec3> pair : OCCUPIED.get(id).values()) {
                    if (pair.getLeft() == blockRideEntity) return pair.getRight();
                }
            }
        }

        return null;
    }*/

    public static boolean isOccupied(Level level, BlockPos pos) {
        ResourceLocation id = getDimensionTypeId(level);

        if (OCCUPIED.containsKey(id) && OCCUPIED.get(id).containsKey(pos)) {
            BlockRideEntity entity = OCCUPIED.get(id).get(pos).getLeft();
            return entity != null && !entity.isRemoved();
        }
        
        return false;
    }

    public static boolean isPlayerSitting(Player player) {
        for (ResourceLocation i : OCCUPIED.keySet()) {
            for (Pair<BlockRideEntity, Vec3> pair : OCCUPIED.get(i).values()) {
                if (pair.getLeft().hasPassenger(player)) return true;
            }
        }

        return false;
    }

    private static ResourceLocation getDimensionTypeId(Level level) {
        return level.dimension().location();
    }
}