package me.itsmcb.vexelcoreproxy;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.commands.Glist;
import me.itsmcb.vexelcoreproxy.commands.VCP;
import me.itsmcb.vexelcoreproxy.commands.CustomCommand;
import me.itsmcb.vexelcoreproxy.utils.FileUtils;
import me.itsmcb.vexelcoreproxy.utils.TimeUtils;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.*;

@Plugin(
        id = "vexelcore",
        name = "VexelCore Proxy",
        version = "@version@",
        description = "A modular and lightweight essential features plugin for the Velocity Minecraft proxy.",
        url = "https://github.com/Vexelosity/VexelCore-Proxy",
        authors = {"ItsMCB"}
)
public class VexelCoreProxy {

    private static ProxyServer server;
    private static Logger logger;
    private static Path dataDirectory;
    private static Toml toml;
    private static VexelCoreProxy instance;

    public static VexelCoreProxy getInstance() { return instance; }
    public static Path getDataDirectory() { return dataDirectory; }
    public static Toml getConfig() { return toml; }
    public static ProxyServer getProxyServer() { return server; }
    public static Logger getLogger() { return logger; }

    @Inject
    public VexelCoreProxy(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        VexelCoreProxy.instance = this;
        VexelCoreProxy.server = server;
        VexelCoreProxy.logger = logger;
        VexelCoreProxy.dataDirectory = dataDirectory;
        loadConfigs(server,false);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            CommandMeta meta_VCP = server.getCommandManager().metaBuilder("vcp").aliases("vexelcoreproxy").build();
            server.getCommandManager().register(meta_VCP,new VCP(server));

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("An error occurred while loading. Shutting down...");
        }
    }
    // Next up: Ability to send custom JSON messages using CC's.






    public static void loadConfigs(ProxyServer server, Boolean reload) {
        long startTime = System.nanoTime();
        if (reload) {
            unloadFeatures(server);
        }
        VexelCoreProxy.toml = FileUtils.getTomlConfig(getDataDirectory(),"config.toml",logger);
        loadFeatures();
        long endTime = System.nanoTime();
        logger.info("All configs have been loaded! Took " + TimeUtils.convertDurationToMs(startTime,endTime) + "ms.");
    }

    public static void setCustomCommands(ProxyServer server) {
        long startTime = System.nanoTime();
        List<HashMap<String, String>> data = toml.getList("customCommand");
        data.forEach((hashMap) -> {
            CommandMeta meta_cc = server.getCommandManager().metaBuilder(hashMap.get("newCommand")).build();
            server.getCommandManager().register(meta_cc, new CustomCommand(server, hashMap));
        });
        long endTime = System.nanoTime();
        logger.info("Loaded " + data.size() + " custom command(s) into memory in " + TimeUtils.convertDurationToMs(startTime,endTime) + "ms.");
    }

    public static void unsetCustomCommands(ProxyServer server) {
        List<HashMap<String, String>> data = toml.getList("customCommand");
        data.forEach((hashMap) -> {
            server.getCommandManager().unregister(hashMap.get("newCommand"));
            logger.debug("UNREGISTERED COMMAND: " + hashMap.get("newCommand"));
        });
    }

    public static void loadFeatures() {
        if (toml.getTable("features").getBoolean("customCommand")) {
            setCustomCommands(server);
        }
        if (toml.getTable("features").getBoolean("customGlist")) {
            try {
                CommandMeta meta_glist = server.getCommandManager().metaBuilder("glist").build();
                server.getCommandManager().register(meta_glist,new Glist(server));
            } catch (Exception e) {
                e.printStackTrace();
                getLogger().error("Unable to enable custom glist due to an error!");
            }
        }
    }

    public static void unloadFeatures(ProxyServer server) {
        unsetCustomCommands(server);
        if (toml.getTable("features").getBoolean("customGlist")) {
            server.getCommandManager().unregister("glist");
        }
    }
}
