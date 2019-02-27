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
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.TextSerializers;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendMessage implements CommandExecutor {
    private CommandSource source;

    private final Pattern urlPattern = Pattern.compile("https?://\\S*");
    @Inject
    private Logger logger;

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {

        String message = Config.getConfNode("SimpleDiscord", "message").getString("message");
        String link = Config.getConfNode("SimpleDiscord", "discordlink").getString("discordlink");

        ArrayList<Text> contents = new ArrayList<Text>();
        contents.add(Text.of(""));
        contents.add(TextSerializers.FORMATTING_CODE.deserialize("&9&l» " + message));
        contents.add(processLinks((Text.of(TextColors.BLUE,TextStyles.BOLD,"» ",TextStyles.RESET,TextColors.GREEN, TextStyles.UNDERLINE,link))));
        contents.add(Text.of(""));


        if(source instanceof Player) {

            Player player = (Player) source;
            PaginationList.builder()
                    .title(Text.of(TextColors.RED,"Discord Server"))
                    .padding(Text.of(TextColors.GRAY,TextStyles.STRIKETHROUGH,"-"))
                    .contents(contents)
                    .build()
                    .sendTo(source);

        } else {

            source.sendMessage(Text.of(TextColors.RED,"Only a player can use this command"));
        }
        return CommandResult.success();
    }

    private final Text processLinks(Text msg) {
        Matcher matcher = urlPattern.matcher(msg.toPlain());
        if (matcher.find()) {
            try {
                return Text.builder().append(msg)
                        .onClick(TextActions.openUrl(new URL(matcher.group())))
                        .build();
            } catch (MalformedURLException e) {
                logger.warn("Error parsing Discord Link. Link: " + matcher.group() + " Error: " + e.getMessage());
            }
        }
        return msg;
    }
}