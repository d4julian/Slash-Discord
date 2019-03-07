package net.dirtcraft.julian;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;

import java.io.File;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

@Plugin(
        id = "slashdiscord",
        name = "Slash Discord",
        version = "1.3.4",
        description = "Creates a /discord command to show a fully customizable Discord link and message.",
        authors = {
                "juliann"
        }
)
public class SlashDiscord {

    @Inject
    private Logger logger;
    @Inject
    private PluginContainer version;

    @Inject
    @DefaultConfig(sharedRoot = true)
    public ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject
    @DefaultConfig(sharedRoot = true)
    public File file;

    private static SlashDiscord instance;

    @Listener
    public void onPreInit(GameInitializationEvent e) {
        Config.setup(file, loader);
        Config.load();
    }

    private CommandSource source;

    @Listener
    public void onGameInit(GameInitializationEvent e) {

        instance = this;
        logger.info("Slash Discord running (version "
                        + version.getVersion().orElse("")
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

        Sponge.getCommandManager().register(instance, command, "discord");
    }

    @Listener
    public void onGameReload(GameReloadEvent e) {
        Config.setup(file, loader);
        Config.load();
    }

}