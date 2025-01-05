//package pl.myku.simplifiedAuth.mixin;
//
//import net.minecraft.core.net.command.CommandManager;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import pl.myku.simplifiedAuth.commands.*;
//
////@Mixin(value = CommandManager.CommandRegistry.class, remap = false)
////final class CommandsMixin {
////    @Inject(method="initCommands", at=@At("TAIL"))
////    private static void initCommands(CallbackInfo ci){
////        Commands.commands.add(new LoginCommand());
////        Commands.commands.add(new RegisterCommand());
////        Commands.commands.add(new VersionCommand());
////        Commands.commands.add(new ChangePasswordCommand());
////        Commands.commands.add(new ResetUserCommand());
////    }
////
////}
