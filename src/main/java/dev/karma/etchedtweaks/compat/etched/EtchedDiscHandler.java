package dev.karma.etchedtweaks.compat.etched;

import gg.moonflower.etched.common.item.EtchedMusicDiscItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.p3pp3rf1y.sophisticatedcore.api.IDiscHandler;
import java.util.Optional;
import java.util.UUID;

/** Registers the public Core API without modifying Etched's item or track serialization. */
public final class EtchedDiscHandler implements IDiscHandler<EtchedMusicDiscItem> {
    public boolean supports(ItemStack stack) { return stack.getItem() instanceof EtchedMusicDiscItem; }
    public Optional<EtchedMusicDiscItem> getSongInfo(ItemStack stack, Level level) {
        return supports(stack) ? Optional.of((EtchedMusicDiscItem) stack.getItem()) : Optional.empty();
    }
    public Optional<Integer> getMusicLengthInTicks(ItemStack stack, Level level) { return Optional.empty(); }
    public Optional<ItemStack> getRandomDisc(RandomSource random) { return Optional.empty(); }
    public int getMusicDiscSize() { return 0; }
    public void playDisc(ServerLevel level, BlockPos pos, UUID storage, ItemStack disc, Runnable callback) {
        EtchedJukeboxSessions.start(level, Vec3.atCenterOf(pos), storage, disc, -1, callback);
    }
    public void playDisc(ServerLevel level, Vec3 pos, UUID storage, ItemStack disc, int entity, Runnable callback) {
        EtchedJukeboxSessions.start(level, pos, storage, disc, entity, callback);
    }
}
