package dev.karma.etchedtweaks.mixin.etched;

import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.SoundBuffer;
import dev.karma.etchedtweaks.compat.etched.PreparedSoundCloudStream;
import net.minecraft.client.sounds.AudioStream;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.io.IOException;

@Mixin(Channel.class)
public abstract class SoundCloudChannelMixin {
    @Unique private SoundBuffer etchedtweaks$ownedBuffer;
    @Inject(method="attachBufferStream",at=@At("HEAD"),cancellable=true)
    private void etchedtweaks$attachPrepared(AudioStream stream,CallbackInfo ci) {
        if(!(stream instanceof PreparedSoundCloudStream prepared))return;
        try {
            etchedtweaks$ownedBuffer=prepared.attachTo((Channel)(Object)this);ci.cancel();
        }catch(IOException error){throw new IllegalStateException("Could not attach prepared SoundCloud audio",error);}
    }
    @Inject(method="destroy",at=@At("TAIL"))
    private void etchedtweaks$releasePrepared(CallbackInfo ci) {
        if(etchedtweaks$ownedBuffer!=null){etchedtweaks$ownedBuffer.discardAlBuffer();etchedtweaks$ownedBuffer=null;}
    }
}
