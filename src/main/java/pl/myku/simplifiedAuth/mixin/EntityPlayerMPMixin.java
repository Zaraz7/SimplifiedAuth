package pl.myku.simplifiedAuth.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.myku.simplifiedAuth.Player;
import pl.myku.simplifiedAuth.SimplifiedAuth;

@Mixin(value = PlayerServer.class, remap = false)
final class EntityPlayerMPMixin {
    @Inject(method="hurt", at = @At("HEAD"), cancellable = true)
    private void authorizationInvulnerability(Entity entity, int i, DamageType type, CallbackInfoReturnable<Boolean> cir){
        Player player = SimplifiedAuth.playerManager.get((PlayerServer) (Object) this);

        if (player != null && !player.isAuthorized()) {
            cir.setReturnValue(false);
        }
    }
}
