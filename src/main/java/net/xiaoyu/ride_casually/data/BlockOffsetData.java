package net.xiaoyu.ride_casually.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.Map;
import java.util.Optional;

public record BlockOffsetData(Optional<String> block_offsets, Optional<Map<String, String>> properties, Coordinates coordinates) {
    public static final Codec<BlockOffsetData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf("block_offsets").forGetter(BlockOffsetData::block_offsets),
        Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties").forGetter(BlockOffsetData::properties),
        Coordinates.CODEC.fieldOf("coordinates").forGetter(BlockOffsetData::coordinates)
    ).apply(instance, BlockOffsetData::new));

    public boolean matches(BlockState state) {
        Block block = state.getBlock();
        boolean matches = true;

        if (this.block_offsets.isPresent()) {
            String blockOffset = this.block_offsets.get();

            if (blockOffset.startsWith("#")) {
                String tagId = blockOffset.substring(1);
                ResourceLocation tagLocation = ResourceLocation.tryParse(tagId);
                TagKey<Block> tagKey = TagKey.create(BuiltInRegistries.BLOCK.key(), tagLocation);
                matches = block.builtInRegistryHolder().is(tagKey);
            } else {
                ResourceLocation blockLocation = BuiltInRegistries.BLOCK.getKey(block);
                ResourceLocation targetLocation = ResourceLocation.tryParse(blockOffset);
                matches = blockLocation.equals(targetLocation);
            }

            if (matches && this.properties.isPresent()) {
                Map<String, String> props = this.properties.get();
                for (Map.Entry<String, String> entry : props.entrySet()) {
                    String property = entry.getKey();
                    String value = entry.getValue();

                    if ("type".equals(property)) {
                        if (state.hasProperty(SlabBlock.TYPE)) {
                            SlabType slabType = state.getValue(SlabBlock.TYPE);
                            matches = slabType.toString().equals(value);
                        } else {
                            matches = false;
                        }
                    }
                }
            }
        }

        return matches;
    }
    
    public record Coordinates(double offset_x, double offset_y, double offset_z) {
        public static final Codec<Coordinates> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("offset_x").forGetter(Coordinates::offset_x),
            Codec.DOUBLE.fieldOf("offset_y").forGetter(Coordinates::offset_y),
            Codec.DOUBLE.fieldOf("offset_z").forGetter(Coordinates::offset_z)
        ).apply(instance, Coordinates::new));
    }
}