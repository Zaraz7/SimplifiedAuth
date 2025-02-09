package pl.myku.simplifiedAuth.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
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
        CommandNode<CommandSource> command = dispatcher.register((ArgumentBuilderLiteral<CommandSource>) (Object) ArgumentBuilderLiteral.literal("simplifiedauth")
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
