package net.dirtcraft.plugin.Commands;

import net.dirtcraft.plugin.ConfigManager;
import net.dirtcraft.plugin.SlashDiscord;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.net.URL;

public class Set implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        URL discordURL = args.<URL>getOne(Text.of("discord link")).get();
        String discordLink = discordURL.toString();

        //try {
            ConfigManager.getConfNode("Discord", "discordlink").setValue(discordLink);
            ConfigManager.save();
            source.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(
                    "&7Your Discord link has &asuccessfully &7been set to &8\"&a&o" + discordLink + "&8&o\""));

            SlashDiscord.getLogger().warn(
                    "The Discord link for " + SlashDiscord.getContainer().getName() + " has been set to \"" + discordLink + "\"");
        /*} catch (MalformedURLException exception) {
            throw new CommandException(TextSerializers.FORMATTING_CODE.deserialize(
                    "&8\"&a&o" + discordLink + "&8\"&7 is &cnot &7a valid link! Please try again"));
        }*/
        return CommandResult.success();
    }

}
