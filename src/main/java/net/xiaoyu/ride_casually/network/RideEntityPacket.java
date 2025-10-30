package net.xiaoyu.ride_casually.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.xiaoyu.ride_casually.RideCasually;

public record RideEntityPacket(int entityId) implements CustomPacketPayload {
    public static final Type<RideEntityPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RideCasually.MOD_ID, "ride_entity"));

    public static final StreamCodec<FriendlyByteBuf, RideEntityPacket> STREAM_CODEC = StreamCodec.of(
        (buf, packet) -> buf.writeInt(packet.entityId),
        buf -> new RideEntityPacket(buf.readInt())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}