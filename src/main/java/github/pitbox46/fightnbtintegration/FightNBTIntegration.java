package github.pitbox46.fightnbtintegration;

import github.pitbox46.fightnbtintegration.network.ClientProxy;
import github.pitbox46.fightnbtintegration.network.CommonProxy;
import github.pitbox46.fightnbtintegration.network.PacketHandler;
import github.pitbox46.fightnbtintegration.network.SSyncConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.PacketDistributor;
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
        github.pitbox46.fightnbtintegration.Config.init(event.getServer().func_240776_a_(new FolderName("epicfightnbt")));
    }

    @SubscribeEvent
    public void onPlayerConnect(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.getPlayer() instanceof ServerPlayerEntity) {
            PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), Config.configFileToSSyncConfig());
        }
    }
}
