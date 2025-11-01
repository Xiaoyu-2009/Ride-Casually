package net.xiaoyu.ride_casually.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.xiaoyu.ride_casually.RideConfig;

import java.util.List;
import java.util.function.Supplier;

public class RidePermissionUtil {

    private static boolean isRidingAllowed(
        boolean globalConfig, 
        Supplier<String> nameSupplier,
        Supplier<List<? extends String>> whitelistSupplier,
        Supplier<List<? extends String>> blacklistSupplier
    ) {

        if (!globalConfig) {
            return false;
        }

        String name = nameSupplier.get();
        if (name == null || name.isEmpty()) {
            return false;
        }

        List<? extends String> whitelist = whitelistSupplier.get();
        if (whitelist != null && !whitelist.isEmpty()) {
            return whitelist.contains(name);
        }

        List<? extends String> blacklist = blacklistSupplier.get();
        if (blacklist != null && !blacklist.isEmpty()) {
            return !blacklist.contains(name);
        }

        return true;
    }
    
    // 实体
    public static boolean isEntityRidingAllowed(Entity entity) {
        return isRidingAllowed(
            RideConfig.ALLOW_ENTITY_RIDING.get(),
            () -> {
                EntityType<?> entityType = entity.getType();
                ResourceLocation entityKey = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
                return entityKey != null ? entityKey.toString() : null;
            },
            RideConfig.ENTITY_RIDING_WHITELIST::get,
            RideConfig.ENTITY_RIDING_BLACKLIST::get
        );
    }

    // 方块
    public static boolean isBlockRidingAllowed(BlockState blockState) {
        return isRidingAllowed(
            RideConfig.ALLOW_BLOCK_RIDING.get(),
            () -> {
                Block block = blockState.getBlock();
                ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(block);
                return blockKey != null ? blockKey.toString() : null;
            },
            RideConfig.BLOCK_RIDING_WHITELIST::get,
            RideConfig.BLOCK_RIDING_BLACKLIST::get
        );
    }
}