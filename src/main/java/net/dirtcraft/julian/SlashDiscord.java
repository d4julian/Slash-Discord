package net.dirtcraft.julian;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;

import java.nio.file.Path;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

@Plugin(
        id = "slashdiscord",
        name = "Slash Discord",
        version = "1.4.0",
        description = "Creates a /discord command to show a fully customizable Discord link and message.",
        authors = {
                "juliann"
        }
)
public class SlashDiscord {

    @Inject
    private Logger logger;
    @Inject
    private PluginContainer container;
    private CommandSource source;
    private static SlashDiscord instance;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path dir;

    @Listener
    public void onPreInit(GameInitializationEvent e) {
        instance = this;
        ConfigManager.setup(dir);
        ConfigManager.load();
        logger.info(container.getName() + "has successfully loaded config files!");
    }

    @Listener
    public void onGameInit(GameInitializationEvent e) {

        logger.info(container.getName() +
                " running (version "
                        + container.getVersion().orElse("")
                        + ")! Hey, I'm alive!");

        CommandSpec reloadCmd = CommandSpec.builder()
                .description(Text.of("Reload command for Slash Discord"))
                .executor(new Reload())
                .permission("slashdiscord.reload")
                .build();
        CommandSpec command = CommandSpec.builder()
                .description(Text.of("Base command for Slash Discord"))
                .executor(new SendMessage())
                .child(reloadCmd, "reload", "rl")
                .build();

        Sponge.getCommandManager().register(this, command, "discord");
    }

    @Listener
    public void onGameReload(GameReloadEvent e) {
        ConfigManager.load();
    }

}