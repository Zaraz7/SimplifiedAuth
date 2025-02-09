package pl.myku.simplifiedAuth.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.core.net.command.*;
import net.minecraft.core.net.packet.*;
import net.minecraft.core.util.helper.AES;
import net.minecraft.server.MinecraftServer;

import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerServer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.myku.simplifiedAuth.SimplifiedAuth;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = PacketHandlerServer.class, remap = false)
abstract class NetServerHandlerMixin {
    private static long teleportTimeout = 0;
    private static final int timeoutBase = SimplifiedAuth.config.getInt("LoginKickTimeout");
    private static int timeout = timeoutBase;
    @Shadow public static org.slf4j.Logger LOGGER = LogUtils.getLogger();
    @Shadow private PlayerServer playerEntity;
    @Shadow private MinecraftServer mcServer;

    @Shadow public abstract void teleportAndRotate(double d, double d1, double d2, float f, float f1);

    @Shadow private int playerInAirTime;

    @Shadow public abstract void kickPlayer(String s);

    @Inject(method="handleBlockDig", at=@At("HEAD"), cancellable = true)
    public void handleBlockDig(PacketPlayerAction packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }

//    @Inject(method="handleFlying", at=@At("HEAD"), cancellable = true)
//    public void handleMovementTypePacket(PacketMovePlayer packet, CallbackInfo ci){
//        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
//            return;
//        }
//        ci.cancel();
//    }

    @Inject(method="handleWindowClick", at=@At("HEAD"), cancellable = true)
    public void handleInventoryAndGui(PacketContainerClick packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method="handleFlying", at=@At("HEAD"), cancellable = true)
    public void handleFlying(PacketMovePlayer packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        if(timeout < 1){
            timeout = timeoutBase;
            this.kickPlayer("Didn't authorize, timeout..");
        }
        if(System.nanoTime() >= (teleportTimeout + 5000000L)){
            playerInAirTime = 0;
            teleportAndRotate(playerEntity.x, playerEntity.y, playerEntity.z, playerEntity.yRot, playerEntity.xRot);
            teleportTimeout = System.nanoTime();
            timeout--;
        }
        ci.cancel();
    }

    @Inject(method="handlePlace", at=@At("HEAD"), cancellable = true)
    public void handlePlace(PacketUseItem packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method="handleChat", at=@At("HEAD"), cancellable = true)
    public void handleChat(PacketChat packet, CallbackInfo ci) throws Exception {
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        try {
            String msg = AES.decrypt(packet.message, AES.keyChain.get(this.playerEntity.username));
            if(msg.contains("/login") || msg.contains("/register") || msg.contains("/reg")){
                return;
            }
        } catch (Exception e) {
            SimplifiedAuth.LOGGER.error(e.getMessage());
        }
        ci.cancel();
    }

    @Inject(method="handleAnimation", at=@At("HEAD"), cancellable = true)
    public void handleAnimation(PacketAnimate packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }

//    @Inject(method="handleEntityAction", at=@At("HEAD"), cancellable = true)
//    public void handleEntityAction(Packet19EntityAction packet, CallbackInfo ci){
//        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
//            return;
//        }
//        ci.cancel();
//    }

    @Inject(method="handleUseEntity", at=@At("HEAD"), cancellable = true)
    public void handleUseEntity(PacketInteract packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method="handleTransaction", at=@At("HEAD"), cancellable = true)
    public void handleTransaction(PacketContainerAck packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }
}
