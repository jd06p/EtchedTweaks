package dev.karma.etchedtweaks;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("etchedtweaks")
public final class EtchedTweaks {
    public EtchedTweaks() {
        if (ModList.get().isLoaded("sophisticatedbackpacksetchedintegration"))
            throw new IllegalStateException("Etched Tweaks replaces Sophisticated Backpacks Etched Integration. Remove its old JAR.");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }
    private void setup(FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("sophisticatedbackpacks") && ModList.get().isLoaded("sophisticatedcore")) {
            event.enqueueWork(dev.karma.etchedtweaks.compat.etched.BackpackIntegration::register);
        } else {
            com.mojang.logging.LogUtils.getLogger().info("Etched Tweaks: SoundCloud fixes active; optional Backpacks integration disabled.");
        }
    }
}
