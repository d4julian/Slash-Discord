package net.dirtcraft.plugin.Commands;

import net.dirtcraft.plugin.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SendMessage implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {

        String message = ConfigManager.getConfNode("Discord", "message").getString();
        String link = ConfigManager.getConfNode("Discord", "discordlink").getString();
        String prefix = ConfigManager.getConfNode("Discord", "prefix").getString();
        String hover = ConfigManager.getConfNode("Discord", "hover").getString();
        String header = ConfigManager.getConfNode("Pagination", "header").getString();
        String padding = ConfigManager.getConfNode("Pagination", "padding").getString();

        ArrayList<Text> contents = new ArrayList<>();
        try {
            contents.add(
                    Text.builder()
                            .append(Text.builder()
                                    .append(TextSerializers.FORMATTING_CODE.deserialize("\n" + prefix + message))
                                    .build())
                            .onHover(TextActions.showText(TextSerializers.FORMATTING_CODE.deserialize(hover)))
                            .onClick(TextActions.openUrl(new URL(link)))
                            .build());
            contents.add(
                    Text.builder()
                            .append(TextSerializers.FORMATTING_CODE.deserialize(prefix + "&a&n" + link + "\n"))
                            .onHover(TextActions.showText(TextSerializers.FORMATTING_CODE.deserialize(hover)))
                            .onClick(TextActions.openUrl(new URL(link)))
                            .build());
        } catch (MalformedURLException ignored) {
            contents.add(
                    Text.builder()
                            .append(Text.builder()
                                    .append(TextSerializers.FORMATTING_CODE.deserialize("\n" + prefix + message))
                                    .build())
                            .onHover(TextActions.showText(TextSerializers.FORMATTING_CODE.deserialize("&cMalformed URL! Check configuration.")))
                            .build());
            contents.add(
                    Text.builder()
                            .append(TextSerializers.FORMATTING_CODE.deserialize(prefix + "&a&n" + link + "\n"))
                            .onHover(TextActions.showText(TextSerializers.FORMATTING_CODE.deserialize("&cMalformed URL! Check configuration.")))
                            .build());
        }


        PaginationList.builder()
                .title(TextSerializers.FORMATTING_CODE.deserialize(header))
                .padding(TextSerializers.FORMATTING_CODE.deserialize(padding))
                .contents(contents)
                .build()
                .sendTo(source);

        return CommandResult.success();
    }

}