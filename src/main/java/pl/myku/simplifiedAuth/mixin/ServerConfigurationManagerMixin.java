package pl.myku.simplifiedAuth.mixin;

import net.minecraft.server.entity.player.EntityPlayerMP;
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
    public void onPlayerConnect(EntityPlayerMP player, CallbackInfo ci){
        String address = player.playerNetServerHandler.netManager.getRemoteAddress().toString();
        if (SimplifiedAuth.dbManager.isSessionValid(player.username, address.substring(1, address.indexOf(':')))){
            Player playerObj = SimplifiedAuth.playerManager.get(player);
            playerObj.authorize();
            player.addChatMessage("greeter.authorized");
        } else {
            player.addChatMessage("greeter.registration");
            player.addChatMessage("greeter.login");
        }
    }
    @Inject(method="playerLoggedOut", at=@At("TAIL"))
    public void onPlayerLoggedOut(EntityPlayerMP player, CallbackInfo ci){
        Player playerObj = SimplifiedAuth.playerManager.get(player);
        if(playerObj.isAuthorized()){
            String address = player.playerNetServerHandler.netManager.getRemoteAddress().toString();
            SimplifiedAuth.dbManager.updateLastSeenAndAddress(player.username, address.substring(1, address.indexOf(':')));
        }
        playerObj.destroy();
    }
}
