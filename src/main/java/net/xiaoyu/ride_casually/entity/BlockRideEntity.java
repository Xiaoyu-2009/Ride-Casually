package net.xiaoyu.ride_casually.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.xiaoyu.ride_casually.RideCasually;
import net.xiaoyu.ride_casually.util.BlockRideUtil;

public class BlockRideEntity extends Entity {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, RideCasually.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<BlockRideEntity>> BLOCK_RIDE_ENTITY = ENTITY_TYPES.register(
    "block_ride_entity", () -> EntityType.Builder.<BlockRideEntity>of(BlockRideEntity::new, MobCategory.MISC)
        .setTrackingRange(256)
        .setUpdateInterval(20)
        .sized(0.0001F, 0.0001F)
        .build("block_ride_entity")
    );

    public BlockRideEntity(EntityType<BlockRideEntity> type, Level level) {
        super(type, level);
    }

    public BlockRideEntity(Level level, BlockPos pos) {
        super(BLOCK_RIDE_ENTITY.get(), level);
        BlockRideUtil.Offset offset = BlockRideUtil.getOffset(level, pos);
        setPos(pos.getX() + offset.x, pos.getY() + offset.y, pos.getZ() + offset.z);
        noPhysics = true;
    }

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        if (passenger instanceof Player player) {
            Vec3 dismountLocation = BlockRideUtil.getDismountLocationForPassenger(player, this);
            if (dismountLocation != null) {
                return dismountLocation;
            }
        }
        
        return super.getDismountLocationForPassenger(passenger);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        BlockRideUtil.removeBlockRideEntity(this);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity);
    }
}