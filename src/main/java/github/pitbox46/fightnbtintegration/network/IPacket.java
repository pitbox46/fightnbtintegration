package github.pitbox46.fightnbtintegration.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public interface IPacket {
    void readPacketData(PacketBuffer buf);

    void writePacketData(PacketBuffer buf);

    void processPacket(NetworkEvent.Context ctx);
}
