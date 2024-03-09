package pl.myku.simplifiedAuth.mixin;

import net.minecraft.core.net.command.*;
import net.minecraft.core.net.packet.*;
import net.minecraft.core.util.helper.AES;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.apache.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.server.net.handler.NetServerHandler;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.myku.simplifiedAuth.SimplifiedAuth;

@Mixin(value = NetServerHandler.class, remap = false)
abstract class NetServerHandlerMixin {

    private static long teleportTimeout = 0;
    private static int timeout = 600;
    @Shadow public static Logger logger = Logger.getLogger("Minecraft");
    @Shadow private EntityPlayerMP playerEntity;
    @Shadow private MinecraftServer mcServer;
    @Shadow public abstract void teleportTo(double d, double d1, double d2, float f, float f1);

    @Shadow private int playerInAirTime;

    @Shadow public abstract void kickPlayer(String s);

    @Inject(method="handleBlockDig", at=@At("HEAD"), cancellable = true)
    public void handleBlockDig(Packet14BlockDig packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method="handleMovementTypePacket", at=@At("HEAD"), cancellable = true)
    public void handleMovementTypePacket(Packet27Position packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method="handleWindowClick", at=@At("HEAD"), cancellable = true)
    public void handleInventoryAndGui(Packet102WindowClick packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method="handleFlying", at=@At("HEAD"), cancellable = true)
    public void handleFlying(Packet10Flying packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        if(timeout < 1){
            timeout = 600;
            this.kickPlayer("Didn't authorize, timeout..");
        }
        if(System.nanoTime() >= (teleportTimeout + 5000000L)){
            playerInAirTime = 0;
            teleportTo(playerEntity.x, playerEntity.y, playerEntity.z, playerEntity.yRot, playerEntity.xRot);
            teleportTimeout = System.nanoTime();
            timeout--;
        }
        ci.cancel();
    }

    @Inject(method="handlePlace", at=@At("HEAD"), cancellable = true)
    public void handlePlace(Packet15Place packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method="handleChat", at=@At("HEAD"), cancellable = true)
    public void handleChat(Packet3Chat packet, CallbackInfo ci) throws Exception {
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        try {
            String msg = AES.decrypt(packet.message, AES.keyChain.get(this.playerEntity.username));
            if(msg.contains("/login") || msg.contains("/register")){
                handleHiddenSlashCommand(msg);
            }
        } catch (Exception e) {
            SimplifiedAuth.LOGGER.error(e.getMessage());
        }
        ci.cancel();
    }
    @Unique
    public void handleHiddenSlashCommand(String s){
        ServerPlayerCommandSender sender = new ServerPlayerCommandSender(this.mcServer, this.playerEntity);
        ServerCommandHandler handler = new ServerCommandHandler(this.mcServer);
        String[] args = s.substring(1).split(" ");
        String[] args1 = new String[args.length - 1];
        System.arraycopy(args, 1, args1, 0, args.length - 1);
        for (Command command : Commands.commands) {
            if (!command.isName(args[0])) continue;
            logger.info("Player " + ((PlayerCommandSender)sender).getPlayer().username + " tried " + args[0]);
            try {
                boolean success = command.execute(handler, sender, args1);
                if (!success) {
                    command.sendCommandSyntax(handler, sender);
                }
            } catch (CommandError e) {
                sender.sendMessage(TextFormatting.RED + e.getMessage());
            } catch (Throwable e) {
                sender.sendMessage(TextFormatting.RED + "Error!");
            }
        }
    }
    @Inject(method="handleAnimation", at=@At("HEAD"), cancellable = true)
    public void handleAnimation(Packet18Animation packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method="handleEntityAction", at=@At("HEAD"), cancellable = true)
    public void handleEntityAction(Packet19EntityAction packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method="handleUseEntity", at=@At("HEAD"), cancellable = true)
    public void handleUseEntity(Packet7UseEntity packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method="handleTransaction", at=@At("HEAD"), cancellable = true)
    public void handleTransaction(Packet106Transaction packet, CallbackInfo ci){
        if(SimplifiedAuth.playerManager.get(playerEntity).isAuthorized()) {
            return;
        }
        ci.cancel();
    }
}
