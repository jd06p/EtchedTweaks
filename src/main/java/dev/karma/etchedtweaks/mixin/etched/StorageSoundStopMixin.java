package dev.karma.etchedtweaks.mixin.etched;
import dev.karma.etchedtweaks.compat.etched.EtchedJukeboxSessions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.UUID;
@Pseudo
@Mixin(targets = "net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.ServerStorageSoundHandler", remap = false)
public abstract class StorageSoundStopMixin {
    @Inject(method = "sendStopMessage", at = @At("HEAD"))
    private static void etchedtweaks$stop(ServerLevel level, Vec3 position, UUID storage, CallbackInfo ci) { EtchedJukeboxSessions.stop(level, storage); }
}
