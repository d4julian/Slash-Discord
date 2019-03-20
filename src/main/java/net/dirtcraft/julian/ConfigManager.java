package net.dirtcraft.julian;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    private static Path dir, config;
    private static CommentedConfigurationNode confNode;
    private static ConfigurationLoader<CommentedConfigurationNode> confLoad;
    private static final String CFG = "Slash-Discord.conf";

    public static void setup(Path folder) {
        dir = folder;
        config = dir.resolve(CFG);
    }

    public static void load() {
        try {
            if(!Files.exists(dir)) {
                Files.createDirectory(dir);

                confLoad = HoconConfigurationLoader.builder().setPath(config).build();
                confNode = confLoad.load();
                addValues();
                save();
            } else {
                confLoad = HoconConfigurationLoader.builder().setPath(config).build();
                confNode = confLoad.load();
                save();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            confLoad.save(confNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void addValues() {

        confNode.getNode("Discord", "message")
                .setValue("&6&lJoin us on Discord")
                .setComment("Customize your message here");
        confNode.getNode("Discord", "discordlink")
                .setValue("https://discord.gg/mGgfyaS")
                .setComment("Insert Discord link here");
        confNode.getNode("Discord", "prefix")
                .setValue("&9&l» ")
                .setComment("Insert prefix before message and link here");
        confNode.getNode("Discord", "hover")
                .setValue("&6Click me to join our Discord!")
                .setComment("Insert text when hovering over link here");
        confNode.getNode("Pagination", "header")
                .setValue("&cDiscord Server")
                .setComment("Insert your pagination header here");
        confNode.getNode("Pagination", "padding")
                .setValue("&7&m-")
                .setComment("Insert your pagination padding here");
    }

    public static CommentedConfigurationNode getConfNode(Object... node) {
        return confNode.getNode(node);
    }

}

