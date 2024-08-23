package pl.myku.simplifiedAuth.commands;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.*;
import net.minecraft.core.net.command.commands.ColorCommand;
import net.minecraft.core.net.packet.Packet72UpdatePlayerProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import pl.myku.simplifiedAuth.SimplifiedAuth;

public class ResetUserCommand extends Command {
    public ResetUserCommand() {
        super("resetuser");
    }

    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        if (args.length == 0) {
            return false;
        } else {
            String name = args[0];
            if (SimplifiedAuth.dbManager.removePlayerFromDatabase(name)) {
                sender.sendMessage("User reset");
                return true;
            } else {
                sender.sendMessage("Failed");
                return false;
            }
        }
    }

    public boolean opRequired(String[] args) {
        return true;
    }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("/resetuser <player>");
    }
}
