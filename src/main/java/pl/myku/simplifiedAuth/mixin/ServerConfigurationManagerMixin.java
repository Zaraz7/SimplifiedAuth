package pl.myku.simplifiedAuth.mixin;

import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.myku.simplifiedAuth.Player;
import pl.myku.simplifiedAuth.SimplifiedAuth;

@Mixin(value = PlayerList.class, remap = false)
final class ServerConfigurationManagerMixin {
    @Inject(method="playerLoggedIn", at=@At("TAIL"))
    public void onPlayerConnect(PlayerServer player, CallbackInfo ci){
        String address = player.playerNetServerHandler.netManager.getRemoteAddress().toString();
        if (SimplifiedAuth.dbManager.isSessionValid(player.username, address.substring(1, address.indexOf(':')))){
            Player playerObj = SimplifiedAuth.playerManager.get(player);
            playerObj.authorize();
            player.sendTranslatedChatMessage("greeter.authorized");
        } else {
            if (SimplifiedAuth.dbManager.isPlayerRegistered(player.username)){
                player.sendTranslatedChatMessage("greeter.login");
            } else {
                player.sendTranslatedChatMessage("greeter.registration");
            }
        }
    }
    @Inject(method="playerLoggedOut", at=@At("TAIL"))
    public void onPlayerLoggedOut(PlayerServer player, CallbackInfo ci){
        Player playerObj = SimplifiedAuth.playerManager.get(player);
        if(playerObj.isAuthorized()){
            String address = player.playerNetServerHandler.netManager.getRemoteAddress().toString();
            SimplifiedAuth.dbManager.updateLastSeenAndAddress(player.username, address.substring(1, address.indexOf(':')));
        }
        playerObj.destroy();
    }
}
