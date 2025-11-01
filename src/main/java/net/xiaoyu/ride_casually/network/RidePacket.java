package net.xiaoyu.ride_casually.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.xiaoyu.ride_casually.RideCasually;

public record RidePacket(int entityId, boolean startRiding, BlockPos blockPos, boolean isBlockRide) implements CustomPacketPayload {
    public static final Type<RidePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RideCasually.MOD_ID, "ride_packet"));

    public static final StreamCodec<FriendlyByteBuf, RidePacket> STREAM_CODEC = StreamCodec.of(
        (buf, packet) -> {
            buf.writeInt(packet.entityId);
            buf.writeBoolean(packet.startRiding);
            buf.writeBlockPos(packet.blockPos);
            buf.writeBoolean(packet.isBlockRide);
        },
        buf -> new RidePacket(buf.readInt(), buf.readBoolean(), buf.readBlockPos(), buf.readBoolean())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}