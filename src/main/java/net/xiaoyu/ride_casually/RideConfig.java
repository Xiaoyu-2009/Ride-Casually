package net.xiaoyu.ride_casually;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class RideConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    
    public static final ModConfigSpec.BooleanValue ALLOW_ENTITY_RIDING;
    public static final ModConfigSpec.BooleanValue ALLOW_BLOCK_RIDING;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ENTITY_RIDING_WHITELIST;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ENTITY_RIDING_BLACKLIST;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> BLOCK_RIDING_WHITELIST;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> BLOCK_RIDING_BLACKLIST;
    
    static {
        BUILDER.push("Ride Config");
        
        ALLOW_ENTITY_RIDING = BUILDER
                .comment("Whether to allow entity riding (是否允许骑乘实体)")
                .define("allowEntityRiding", true);
                
        ALLOW_BLOCK_RIDING = BUILDER
                .comment("Whether to allow block riding (是否允许骑乘方块)")
                .define("allowBlockRiding", true);
        
        ENTITY_RIDING_WHITELIST = BUILDER
                .comment("Entity riding whitelist (实体骑乘-白名单)")
                .defineListAllowEmpty("entityRidingWhitelist", ArrayList::new, obj -> obj instanceof String);
                
        ENTITY_RIDING_BLACKLIST = BUILDER
                .comment("Entity riding blacklist (实体骑乘-黑名单)")
                .defineListAllowEmpty("entityRidingBlacklist", ArrayList::new, obj -> obj instanceof String);
                
        BLOCK_RIDING_WHITELIST = BUILDER
                .comment("Block riding whitelist (方块骑乘-白名单)")
                .defineListAllowEmpty("blockRidingWhitelist", ArrayList::new, obj -> obj instanceof String);
                
        BLOCK_RIDING_BLACKLIST = BUILDER
                .comment("Block riding blacklist (方块骑乘-黑名单)")
                .defineListAllowEmpty("blockRidingBlacklist", ArrayList::new, obj -> obj instanceof String);
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}