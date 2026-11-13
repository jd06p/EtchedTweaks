package dev.karma.etchedtweaks.smoke;

import dev.karma.etchedtweaks.compat.etched.SoundCloudDuration;
import gg.moonflower.etched.common.sound.download.SoundCloudSource;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

/** Included only with -Psmoke; never shipped in the release JAR. */
@Mod.EventBusSubscriber(modid="etchedtweaks")
public final class StartupCheck {
    @SubscribeEvent public static void started(ServerStartedEvent event) throws Exception {
        if (!((Object)new SoundCloudSource() instanceof SoundCloudDuration))
            throw new AssertionError("SoundCloud source mixin did not apply");
        var source=new SoundCloudSource();
        if(!source.isValidUrl("https://ON.SoundCloud.com/token") || source.isValidUrl("https://notsoundcloud.com/song")
                || source.isValidUrl("https://bandcamp.com/song") || source.isValidUrl("https://cdn.discordapp.com/song.mp3"))
            throw new AssertionError("SoundCloud host/provider boundary changed");
        Class.forName("gg.moonflower.etched.common.menu.EtchingMenu");
        if (ModList.get().isLoaded("sophisticatedbackpacks")) BackpackStartupCheck.verify(event.getServer().overworld());
        System.out.println("ETCHED_TWEAKS_SMOKE_PASS: dedicated server started; SoundCloud mixin applied; backpacks="
                + ModList.get().isLoaded("sophisticatedbackpacks"));
        event.getServer().execute(() -> event.getServer().halt(false));
    }
}
