package net.dirtcraft.julian;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Reload implements CommandExecutor {

        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6Hey! I'm going to reload Slash Discord"));
            ConfigManager.load();
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&aSlash Discord was reloaded successfully!"));
            return CommandResult.success();
        }
}
