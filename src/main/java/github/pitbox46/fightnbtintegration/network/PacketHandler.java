package github.pitbox46.fightnbtintegration.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Function;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "3.2.1";
    public static SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("fightnbtintegration", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);
    private static int ID = 0;

    public static void init() {
        registerPacket(SSyncConfig.class, pb -> new SSyncConfig().readPacketData(pb));
    }

    public static <T extends IPacket> void registerPacket(Class<T> packetClass, Function<PacketBuffer,T> decoder) {
        CHANNEL.registerMessage(
                ID++,
                packetClass,
                IPacket::writePacketData,
                decoder,
                (msg, ctx) -> {
                    ctx.get().enqueueWork(() -> msg.processPacket(ctx.get()));
                    ctx.get().setPacketHandled(true);
                }
        );
    }
}
