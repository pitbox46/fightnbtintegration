package github.pitbox46.fightnbtintegration.network;

import github.pitbox46.fightnbtintegration.Config;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SSyncConfig implements IPacket {
    public String json;

    public SSyncConfig(String json) {
        this.json = json;
    }
    public SSyncConfig() {}

    @Override
    public SSyncConfig readPacketData(PacketBuffer buf) {
        json = buf.readString();
        return this;
    }

    @Override
    public void writePacketData(PacketBuffer buf) {
        buf.writeString(json, json.length());
    }

    @Override
    public void processPacket(NetworkEvent.Context ctx) {
        Config.readConfig(this.json);
    }
}
