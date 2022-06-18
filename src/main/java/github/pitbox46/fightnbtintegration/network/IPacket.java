package github.pitbox46.fightnbtintegration.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public interface IPacket {
    IPacket readPacketData(FriendlyByteBuf buf);

    void writePacketData(FriendlyByteBuf buf);

    void processPacket(NetworkEvent.Context ctx);
}
