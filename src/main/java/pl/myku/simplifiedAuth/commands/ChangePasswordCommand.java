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
import pl.myku.simplifiedAuth.SimplifiedAuth;

public class ChangePasswordCommand implements CommandManager.CommandRegistry {
    private static final SimpleCommandExceptionType UNKNOWN_ERROR = new SimpleCommandExceptionType(() -> {
        return I18n.getInstance().translateKey("greeter.unknown_error");
    });
    private static final SimpleCommandExceptionType NOT_AUTHORIZED = new SimpleCommandExceptionType(() -> {
        return I18n.getInstance().translateKey("greeter.not_authorized");
    });
    private static final SimpleCommandExceptionType PASSWORDS_NOT_MATCH = new SimpleCommandExceptionType(() -> {
        return I18n.getInstance().translateKey("greeter.passwords_not_match");
    });

    public ChangePasswordCommand() {
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        CommandNode<CommandSource> command = dispatcher.register((ArgumentBuilderLiteral<CommandSource>) (Object) ArgumentBuilderLiteral.literal("changepassword")
                .then(ArgumentBuilderRequired.argument("password", ArgumentTypeString.word())
                        .then(ArgumentBuilderRequired.argument("passwordConfirm", ArgumentTypeString.word())
                            .executes((c) -> {
                                CommandSource source = (CommandSource)c.getSource();
                                Player player = source.getSender();
                                String arg1 = c.getArgument("password", String.class);
                                String arg2 = c.getArgument("passwordConfirm", String.class);
                                if(player == null) {
                                    throw UNKNOWN_ERROR.create();
                                }

                                if(!SimplifiedAuth.playerManager.get(player).isAuthorized()){
                                    throw NOT_AUTHORIZED.create();
                                }
                                if(arg1.equals(arg2)){
                                    SimplifiedAuth.dbManager.changePassword(player.username, arg1);
                                    player.sendTranslatedChatMessage("greeter.password_changed");
                                    return Command.SINGLE_SUCCESS;
                                }
                                else{
                                    throw PASSWORDS_NOT_MATCH.create();
                                }
                            })
                        )
                )
        );
    }
}