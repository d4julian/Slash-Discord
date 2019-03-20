package net.dirtcraft.julian;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SendMessage implements CommandExecutor {
    private CommandSource source;

    @Inject
    private Logger logger;

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {

        String message = ConfigManager.getConfNode("Discord", "message").getString();
        String link = ConfigManager.getConfNode("Discord", "discordlink").getString();
        String prefix = ConfigManager.getConfNode("Discord", "prefix").getString();
        String header = ConfigManager.getConfNode("Pagination", "header").getString();
        String padding = ConfigManager.getConfNode("Pagination", "padding").getString();
        String hover = ConfigManager.getConfNode("Discord", "hover").getString();

        ArrayList<Text> contents = new ArrayList<Text>();
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
                .append(Text.builder()
                .append(TextSerializers.FORMATTING_CODE.deserialize(prefix + "&a&n" + link + "\n"))
                        .build())
                .onHover(TextActions.showText(TextSerializers.FORMATTING_CODE.deserialize(hover)))
                .onClick(TextActions.openUrl(new URL(link)))
                .build());
        } catch (MalformedURLException ignored) {}


        if(source instanceof Player) {

            PaginationList.builder()
                    .title(TextSerializers.FORMATTING_CODE.deserialize(header))
                    .padding(TextSerializers.FORMATTING_CODE.deserialize(padding))
                    .contents(contents)
                    .build()
                    .sendTo(source);

        } else {
            logger.error("Slash Discord can only be used in-game!");
        }
        return CommandResult.success();
    }

}