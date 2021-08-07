package github.pitbox46.fightnbtintegration.network;

import com.google.gson.JsonObject;
import github.pitbox46.fightnbtintegration.Config;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Function;

public class SSyncConfig implements IPacket {
    public JsonObject json;

    public SSyncConfig(JsonObject json) {
        this.json = json;
    }
    public SSyncConfig() {}

    @Override
    public void readPacketData(PacketBuffer buf) {
        this.json = JSONUtils.fromJson(buf.readString());
    }

    @Override
    public void writePacketData(PacketBuffer buf) {
        buf.writeString(this.json.toString());
    }

    @Override
    public void processPacket(NetworkEvent.Context ctx) {
        Config.readJson(this.json);
    }

    public static Function<PacketBuffer,SSyncConfig> decoder() {
        return packetBuffer -> {
            SSyncConfig packet = new SSyncConfig();
            packet.readPacketData(packetBuffer);
            return packet;
        };
    }
}
