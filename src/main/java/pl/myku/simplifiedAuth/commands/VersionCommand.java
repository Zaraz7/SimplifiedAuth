package pl.myku.simplifiedAuth.commands;
import net.minecraft.core.net.command.*;

public class VersionCommand extends Command {
    public VersionCommand() {super("simplifiedauth", "sa");}

    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        if(sender.getPlayer() != null) {
            sender.sendMessage("Simplified Auth: Version 1.7.7.0-3.0");
            return true;
        }
        else{
            return false;
        }
    }

    public boolean opRequired(String[] args) {
        return false;
    }

    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
        sender.sendMessage("/version");
    }
}
