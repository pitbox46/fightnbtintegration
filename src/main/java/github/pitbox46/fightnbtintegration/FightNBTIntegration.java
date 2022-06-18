package github.pitbox46.fightnbtintegration;

import github.pitbox46.fightnbtintegration.network.ClientProxy;
import github.pitbox46.fightnbtintegration.network.CommonProxy;
import github.pitbox46.fightnbtintegration.network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("fightnbtintegration")
public class FightNBTIntegration {
    private static final Logger LOGGER = LogManager.getLogger();
    public static CommonProxy PROXY;

    public FightNBTIntegration() {
        PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        github.pitbox46.fightnbtintegration.Config.init(event.getServer().getWorldPath(new LevelResource("epicfightnbt")));
    }

    @SubscribeEvent
    public void onPlayerConnect(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.getPlayer() instanceof ServerPlayer) {
            PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getPlayer()), Config.configFileToSSyncConfig());
        }
    }
}
