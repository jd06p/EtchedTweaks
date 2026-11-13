package dev.karma.etchedtweaks.mixin;

import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import java.util.List;
import java.util.Set;

/** Uses mod metadata only: never probes or loads an optional mod's classes. */
public final class OptionalMixinPlugin implements IMixinConfigPlugin {
    public void onLoad(String mixinPackage) {}
    public String getRefMapperConfig() { return null; }
    public boolean shouldApplyMixin(String target, String mixin) {
        if (mixin.endsWith("JukeboxUpgradeLimitMixin") || mixin.endsWith("StorageSoundAccessor") || mixin.endsWith("StorageSoundStopMixin"))
            return LoadingModList.get().getModFileById("sophisticatedbackpacks") != null
                    && LoadingModList.get().getModFileById("sophisticatedcore") != null;
        return true;
    }
    public void acceptTargets(Set<String> mine, Set<String> others) {}
    public List<String> getMixins() { return null; }
    public void preApply(String target, ClassNode node, String mixin, IMixinInfo info) {}
    public void postApply(String target, ClassNode node, String mixin, IMixinInfo info) {}
}
