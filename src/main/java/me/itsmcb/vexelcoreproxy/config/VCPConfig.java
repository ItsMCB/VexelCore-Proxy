package me.itsmcb.vexelcoreproxy.config;

import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.config.language.en_US;
import me.itsmcb.vexelcoreproxy.config.main.MainConfig;
import me.itsmcb.vexelcoreproxy.utils.TimeUtils;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

public class VCPConfig {
    private VexelCoreProxy instance;
    private String fileName;
    private Path dataDirectory;
    private CommentedConfigurationNode yamlConfig;
    private YamlConfigurationLoader loader;

    public VCPConfig(VexelCoreProxy instance, Path dataDirectory, String fileName) {
        long startTime = System.nanoTime();
        this.instance = instance;
        this.fileName = fileName;
        this.dataDirectory = dataDirectory;
        this.loader = YamlConfigurationLoader.builder()
                .path(Path.of(dataDirectory + "/" + fileName + ".yml"))
                .nodeStyle(NodeStyle.BLOCK) // Block is easier for the end user to work with
                .build();
        load();
        long endTime = System.nanoTime();
        instance.getLogger().info("Configuration file \"" + fileName + ".yml\" has been loaded successfully. Took " + TimeUtils.convertDurationToMs(startTime,endTime));
    }

    public void set(CommentedConfigurationNode yamlConfig) {
        this.yamlConfig = yamlConfig;
    }

    public CommentedConfigurationNode get() {
        return yamlConfig;
    }

    //
    // Loading and Saving Config
    // Based on https://github.com/SpongePowered/Configurate/wiki/Getting-Started
    //

    public void load() {
        CommentedConfigurationNode root;
        try {
            root = loader.load();
            // Sets default config options
            if (fileName.equalsIgnoreCase("config")) {
                root.get(MainConfig.class);
            }
            // Sets default language file values
            if (fileName.startsWith("language")) {
                if (fileName.endsWith("en_US")) {
                    root.get(en_US.class);
                }
            }
        } catch (IOException e) {
            instance.getLogger().warn("An error occurred while loading the \"" + fileName + ".yml\" configuration! " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
            return;
        }
        try {
            loader.save(root);
        } catch (final ConfigurateException e) {
            instance.getLogger().warn("Unable to save the \"" + fileName + ".yml\" configuration! Sorry! " + e.getMessage());
            System.exit(1);
        }
        set(root);
    }

    public void save() {
        try {
            loader.save(get());
        } catch (final ConfigurateException e) {
            instance.getLogger().warn("Unable to save the \"" + fileName + ".yml\" configuration! Sorry! " + e.getMessage());
            System.exit(1);
        }
    }

}
