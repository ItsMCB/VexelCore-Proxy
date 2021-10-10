package me.itsmcb.vexelcoreproxy.utils;

import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class VCPConfigTest {

    public static void loadConfigTest(VexelCoreProxy instance, Path dataDirectory, String fileName) throws SerializationException {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of(dataDirectory + fileName)) // Set where we will load and save to
                .build();
        CommentedConfigurationNode root;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            System.exit(1);
            return;
        }
        try {
            loader.save(root);
        } catch (final ConfigurateException e) {
            System.err.println("Unable to save your messages configuration! Sorry! " + e.getMessage());
            System.exit(1);
        }
        //instance.setYamlConfig(root); // Set object
    }

    public static void saveConfig(VexelCoreProxy instance, Path dataDirectory, String fileName, CommentedConfigurationNode root) throws SerializationException {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(Path.of(dataDirectory + fileName)) // Set where we will load and save to
                .build();
        try {
            loader.save(root);
        } catch (final ConfigurateException e) {
            System.err.println("Unable to save configuration! Sorry! " + e.getMessage());
            System.exit(1);
        }
        //instance.setYamlConfig(root); // Set object
    }


    public static void loadConfigTest2(VexelCoreProxy instance, Path dataDirectory, String fileName) throws ConfigurateException {
        final Path file = Paths.get(dataDirectory + fileName);
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(file)
                .build();

        final CommentedConfigurationNode node = loader.load(); // Load from file
        final MyConfiguration config = node.get(MyConfiguration.class); // Populate object

        // Do whatever actions with the configuration, then...
        config.itemName("Nerd 420");

        node.set(MyConfiguration.class, config); // Update the backing node
        loader.save(node); // Write to the original file
    }

    @ConfigSerializable
    static class MyConfiguration {

        // Fields must be non-final to be modified

        @Comment("Here is a comment to describe the purpose of this field")
        private Pattern filter = Pattern.compile("cars?"); // Set defaults by initializing the field

        // As long as custom classes are annotated with @ConfigSerializable, they can be nested as ordinary fields.
        private List<Section> sections = new ArrayList<>();

        // This won't be written to the file because it's marked as `transient`
        private transient @MonotonicNonNull String decoratedName;

        private String itemName;

        public String itemName() {
            return this.itemName;
        }

        public void itemName(final String itemName) {
            this.itemName = itemName;
        }

        public Pattern filter() {
            return this.filter;
        }

        public List<Section> sections() {
            return this.sections;
        }

        public String decoratedItemName() {
            if (this.decoratedName == null) {
                this.decoratedName = "[" + this.itemName + "]";
            }
            return this.decoratedName;
        }

    }

    @ConfigSerializable
    static class Section {

        private String name;
        private UUID id;

        // the ObjectMapper resolves settings based on fields -- these methods are provided as a convenience
        public String name() {
            return this.name;
        }

        public UUID id() {
            return this.id;
        }

    }
}
