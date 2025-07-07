package pl.myku.simplifiedAuth.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeString;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.server.net.command.ConsoleCommandSource;
import net.minecraft.server.net.command.ServerCommandSource;
import pl.myku.simplifiedAuth.SimplifiedAuth;

public class ResetUserCommand implements CommandManager.CommandRegistry {
    private static final SimpleCommandExceptionType NO_SUCH_USER = new SimpleCommandExceptionType(() -> {
        return I18n.getInstance().translateKey("greeter.no_such_user");
    });

    public ResetUserCommand() {
    }


    public void register(CommandDispatcher<CommandSource> dispatcher) {
        CommandNode<CommandSource> command = dispatcher.register((ArgumentBuilderLiteral<CommandSource>) (Object) ArgumentBuilderLiteral.literal("resetuser")
                .requires((c) -> {return ((ServerCommandSource) c).hasAdmin();})
                .then(ArgumentBuilderRequired.argument("player", ArgumentTypeString.word())
                        .executes((c) -> {
                            CommandSource source = (CommandSource)c.getSource();
                            Player player = source.getSender();
                            String argumentPlayer = c.getArgument("player", String.class);
                            if (SimplifiedAuth.dbManager.removePlayerFromDatabase(argumentPlayer)) {
                                player.sendTranslatedChatMessage("greeter.user_reset_successful");
                                return Command.SINGLE_SUCCESS;
                            } else {
                                throw NO_SUCH_USER.create();
                            }
                        })
                )
        );
    }
}
