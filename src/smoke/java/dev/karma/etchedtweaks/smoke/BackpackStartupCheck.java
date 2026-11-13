package dev.karma.etchedtweaks.smoke;

import dev.karma.etchedtweaks.compat.etched.EtchedDiscHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.DiscHandlerRegistry;
import dev.karma.etchedtweaks.compat.etched.EtchedJukeboxSessions;
import dev.karma.etchedtweaks.mixin.etched.StorageSoundAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeItem;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

final class BackpackStartupCheck {
    static void verify(ServerLevel level) throws Exception {
        long handlers=DiscHandlerRegistry.getHandlers().stream().filter(h -> h instanceof EtchedDiscHandler).count();
        if (handlers != 1) throw new AssertionError("Expected exactly one Etched Tweaks disc handler; got " + handlers);
        System.out.println("PASS: optional Core registry contains exactly one Etched Tweaks disc handler.");
        AtomicInteger callbacks=new AtomicInteger();
        UUID first=UUID.randomUUID(), second=UUID.randomUUID();
        EtchedJukeboxSessions.start(level, Vec3.ZERO, first, ItemStack.EMPTY, -1, callbacks::incrementAndGet);
        StorageSoundAccessor.etchedtweaks$finish(level, first, true);
        if(callbacks.get()!=1) throw new AssertionError("Native completion did not advance exactly once");
        EtchedJukeboxSessions.start(level, Vec3.ZERO, first, ItemStack.EMPTY, -1, callbacks::incrementAndGet);
        EtchedJukeboxSessions.start(level, Vec3.ZERO, second, ItemStack.EMPTY, -1, callbacks::incrementAndGet);
        EtchedJukeboxSessions.stopped(null);
        if(callbacks.get()!=1) throw new AssertionError("Shutdown advanced the playlist");
        System.out.println("PASS: native completion invoker, stop mixin and two-session shutdown cleanup.");
        var storageItem=ForgeRegistries.ITEMS.getValue(new ResourceLocation("sophisticatedstorage:advanced_jukebox_upgrade"));
        if(storageItem instanceof IUpgradeItem<?> storage) {
            var backpack=ForgeRegistries.ITEMS.getValue(new ResourceLocation("sophisticatedbackpacks:netherite_backpack"));
            Object wrapper=Class.forName("net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper")
                    .getConstructor(ItemStack.class).newInstance(new ItemStack(backpack));
            Object upgrades=wrapper.getClass().getMethod("getUpgradeHandler").invoke(wrapper);
            boolean valid=(Boolean)net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeHandler.class.getMethod("isItemValid",int.class,ItemStack.class)
                    .invoke(upgrades,0,new ItemStack(storageItem));
            if(!valid) throw new AssertionError("Storage jukebox still rejected by backpack tag validation");
            var nativeItem=(IUpgradeItem<?>)ForgeRegistries.ITEMS.getValue(new ResourceLocation("sophisticatedbackpacks:advanced_jukebox_upgrade"));
            if(storage.getUpgradesPerStorage("backpack")!=nativeItem.getUpgradesPerStorage("backpack")
                    || storage.getUpgradesInGroupPerStorage("backpack")!=nativeItem.getUpgradesInGroupPerStorage("backpack"))
                throw new AssertionError("Backpack native limits not preserved");
            System.out.println("PASS: actual backpack wrapper accepts Storage jukebox; item/group limits match native Backpacks.");
        }
    }
}
