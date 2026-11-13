package dev.karma.etchedtweaks.compat.etched;

import net.minecraftforge.common.MinecraftForge;
import net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.DiscHandlerRegistry;

/** Loaded only after both optional Sophisticated mods have been detected. */
public final class BackpackIntegration {
    private BackpackIntegration() {}
    public static void register() {
        EtchedJukeboxNetwork.register();
        DiscHandlerRegistry.registerHandler(new EtchedDiscHandler());
        MinecraftForge.EVENT_BUS.register(EtchedJukeboxSessions.class);
        com.mojang.logging.LogUtils.getLogger().info("Etched Tweaks: registered optional Backpacks disc handler and playback sessions.");
    }
}
