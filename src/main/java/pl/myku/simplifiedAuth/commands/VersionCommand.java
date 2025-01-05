package pl.myku.simplifiedAuth.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import pl.myku.simplifiedAuth.SimplifiedAuth;

public class VersionCommand implements CommandManager.CommandRegistry {
    private static final SimpleCommandExceptionType UNKNOWN_ERROR = new SimpleCommandExceptionType(() -> {
        return I18n.getInstance().translateKey("greeter.unknown_error");
    });

    public VersionCommand() {
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        CommandNode<CommandSource> command = dispatcher.register((LiteralArgumentBuilder<CommandSource>) (Object) LiteralArgumentBuilder.literal("simplifiedauth")
            .executes((c) -> {
                CommandSource source = (CommandSource)c.getSource();
                Player player = source.getSender();
                if(player != null) {
                    player.sendMessage("Simplified Auth: Version 7.3-1.0");
                    return Command.SINGLE_SUCCESS;
                }
                else{
                    throw UNKNOWN_ERROR.create();
                }
            })
        );
    }
}

//import net.minecraft.core.net.command.*;
//
//public class VersionCommand extends Command {
//    public VersionCommand() {super("simplifiedauth", "sa");}
//
//    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
//        if(sender.getPlayer() != null) {
//            sender.sendMessage("Simplified Auth: Version 7.1-3.0");
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
//
//    public boolean opRequired(String[] args) {
//        return false;
//    }
//
//    public void sendCommandSyntax(CommandHandler handler, CommandSender sender) {
//        sender.sendMessage("/version");
//    }
//}
