package net.xiaoyu.ride_casually.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import net.xiaoyu.ride_casually.RideCasually;
import net.xiaoyu.ride_casually.util.BlockRideUtil;

import java.util.*;

public class BlockOffsetManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final BlockOffsetManager INSTANCE = new BlockOffsetManager();
    
    private final List<BlockOffsetData> blockOffsets = new ArrayList<>();
    private final Map<BlockState, BlockRideUtil.Offset> blockStateCache = new HashMap<>();

    private BlockOffsetManager() {
        super(GSON, "block_offsets");
    }

    public static BlockOffsetManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.blockOffsets.clear();
        this.blockStateCache.clear();
        
        RegistryOps<JsonElement> registryOps = RegistryOps.create(
            JsonOps.INSTANCE, RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY)
        );

        List<BlockOffsetData> specificOffsets = new ArrayList<>();
        List<BlockOffsetData> defaultOffsets = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            try {
                BlockOffsetData data = BlockOffsetData.CODEC.parse(registryOps, entry.getValue()).getOrThrow();
                if (data.block_offsets().isPresent()) {
                    specificOffsets.add(data);
                } else {
                    defaultOffsets.add(data);
                }
            } catch (Exception e) {
                // 以防万一虽然我平时不打印日志.....
                RideCasually.LOGGER.error("Error loading block offset data: {}", e.getMessage());
            }
        }

        this.blockOffsets.addAll(specificOffsets);
        this.blockOffsets.addAll(defaultOffsets);
    }

    public BlockRideUtil.Offset getOffsetForBlock(BlockState state) {
        BlockRideUtil.Offset cached = this.blockStateCache.get(state);
        if (cached != null) {
            return cached;
        }

        for (BlockOffsetData data : this.blockOffsets) {
            if (data.matches(state)) {
                BlockRideUtil.Offset offset = new BlockRideUtil.Offset(
                    data.coordinates().offset_x(), 
                    data.coordinates().offset_y(), 
                    data.coordinates().offset_z()
                );

                this.blockStateCache.put(state, offset);
                
                return offset;
            }
        }

        return null;
    }
}