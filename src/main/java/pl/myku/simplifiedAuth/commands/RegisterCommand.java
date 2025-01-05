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


public class RegisterCommand implements CommandManager.CommandRegistry {
    private static final SimpleCommandExceptionType UNKNOWN_ERROR = new SimpleCommandExceptionType(() -> {
        return I18n.getInstance().translateKey("greeter.unknown_error");
    });
    private static final SimpleCommandExceptionType PASSWORDS_NOT_MATCH = new SimpleCommandExceptionType(() -> {
        return I18n.getInstance().translateKey("greeter.passwords_not_match");
    });

    public RegisterCommand() {

    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        CommandNode<CommandSource> command = dispatcher.register((LiteralArgumentBuilder<CommandSource>) (Object) LiteralArgumentBuilder.literal("register")
                .then(RequiredArgumentBuilder.argument("password", StringArgumentType.word())
                        .then(RequiredArgumentBuilder.argument("passwordConfirm", StringArgumentType.word())
                                .executes((c) -> {
                                    CommandSource source = (CommandSource)c.getSource();
                                    Player player = source.getSender();
                                    String arg1 = c.getArgument("password", String.class);
                                    String arg2 = c.getArgument("passwordConfirm", String.class);
                                    if(SimplifiedAuth.dbManager.isPlayerRegistered(player.username)){
                                        player.sendTranslatedChatMessage("greeter.already_registered");
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if (arg1.equals(arg2)) {
                                        SimplifiedAuth.dbManager.addPlayerToDatabase(player.username, arg1);
                                        if(SimplifiedAuth.dbManager.isPlayerRegistered(player.username)){
                                            SimplifiedAuth.playerManager.get(player).authorize();
                                            player.sendTranslatedChatMessage("greeter.registered_successfully");
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        throw UNKNOWN_ERROR.create();
                                    } else {
                                        throw PASSWORDS_NOT_MATCH.create();
                                    }
                                })
                        )
                )
        );
        dispatcher.register( (LiteralArgumentBuilder<CommandSource>) (Object) LiteralArgumentBuilder.literal("reg").redirect((CommandNode <Object>) (Object) command));
    }
}
