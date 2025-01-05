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

public class LoginCommand implements CommandManager.CommandRegistry {
    private static final SimpleCommandExceptionType NOT_REGISTERED = new SimpleCommandExceptionType(() -> {
        return I18n.getInstance().translateKey("greeter.not_registered");
    });
    private static final SimpleCommandExceptionType ALREADY_LOGGED_IN = new SimpleCommandExceptionType(() -> {
        return I18n.getInstance().translateKey("greeter.already_logged_in");
    });
    private static final SimpleCommandExceptionType WRONG_PASSWORD = new SimpleCommandExceptionType(() -> {
        return I18n.getInstance().translateKey("greeter.wrong_password");
    });

    public LoginCommand() {
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        CommandNode<CommandSource> command = dispatcher.register((LiteralArgumentBuilder<CommandSource>) (Object) LiteralArgumentBuilder.literal("login")
                .then(RequiredArgumentBuilder.argument("password", StringArgumentType.word())
                        .executes((c) -> {
                            CommandSource source = (CommandSource)c.getSource();
                            Player player = source.getSender();
                            if(!SimplifiedAuth.dbManager.isPlayerRegistered(player.username)){
                                throw NOT_REGISTERED.create();
                            }
                            if(SimplifiedAuth.playerManager.get(player).isAuthorized()){
                                throw ALREADY_LOGGED_IN.create();
                            }
                            else if(
                                    SimplifiedAuth.dbManager.loginPlayer(
                                            player.username,
                                            c.getArgument("password", String.class)
                                    )
                            ){
                                SimplifiedAuth.playerManager.get(player).authorize();
                                player.sendTranslatedChatMessage("greeter.authorized");
                                return Command.SINGLE_SUCCESS;
                            }
                            else{
                                throw WRONG_PASSWORD.create();
                            }
                        })
                )
        );
    }
}


