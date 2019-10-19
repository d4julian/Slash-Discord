package net.dirtcraft.plugin;

import com.google.inject.Inject;
import net.dirtcraft.plugin.Commands.SendMessage;
import net.dirtcraft.plugin.Commands.Set;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;

@Plugin(
        id = "slashdiscord",
        name = "Slash Discord",
        version = "1.4.1",
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

    private static SlashDiscord instance;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path dir;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        instance = this;

        ConfigManager.setup(dir);
        ConfigManager.load();
        logger.info(container.getName() + "has successfully loaded config files!");
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {

        logger.info(container.getName() +
                " running (version "
                        + container.getVersion().orElse("UNSTABLE")
                        + ")! Hey, I'm alive!");

        CommandSpec reload = CommandSpec.builder()
                .description(Text.of("Reload command for " + container.getName()))
                .executor(new Reload())
                .permission("slashdiscord.reload")
                .build();

        CommandSpec set = CommandSpec.builder()
                .description(Text.of("Set command for " + container.getName()))
                .executor(new Set())
                .permission("slashdiscord.set")
                .arguments(GenericArguments.url(Text.of("discord link")))
                .build();

        CommandSpec base = CommandSpec.builder()
                .description(Text.of("Base command for " + container.getName()))
                .executor(new SendMessage())
                .child(set, "set")
                .child(reload, "reload")
                .build();

        Sponge.getCommandManager().register(this, base, "discord");
    }

    @Listener
    public void onGameReload(GameReloadEvent event) {
        ConfigManager.load();
    }

    public static Logger getLogger() {
        return instance.logger;
    }

    public static PluginContainer getContainer() {
        return instance.container;
    }

}
