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
import me.itsmcb.vexelcoreproxy.commands.MainCMD;
import me.itsmcb.vexelcoreproxy.commands.CustomCommand;
import me.itsmcb.vexelcoreproxy.utils.FileUtils;
import me.itsmcb.vexelcoreproxy.utils.TimeUtils;
import me.itsmcb.vexelcoreproxy.utils.VelocityUtils;
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
    private Toml toml;
    private static VexelCoreProxy instance;

    public static Path getDataDirectory() { return dataDirectory; }
    public Toml getConfig() {
        return toml;
    }
    public ProxyServer getProxyServer() { return server; }
    public Logger getLogger() { return logger; }

    @Inject
    public VexelCoreProxy(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        VexelCoreProxy.instance = this;
        VexelCoreProxy.server = server;
        VexelCoreProxy.logger = logger;
        VexelCoreProxy.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            loadConfigs(instance,false);
            VelocityUtils.registerCommand(new String[] {"vcp","vexelcoreproxy"},new MainCMD(instance),instance);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("An error occurred while loading. Shutting down...");
        }
        /*
        try {
            CommandMeta meta_VCP = server.getCommandManager().metaBuilder("vcp").aliases("vexelcoreproxy").build();
            server.getCommandManager().register(meta_VCP,new VCP(server));

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("An error occurred while loading. Shutting down...");
        }

         */
    }
    // Next up: Ability to send custom JSON messages using CC's.
    // By default, only show the /glist servers if there are players in them. The whole list can be viewed with a flag. Also, show hover info for servers and players






    public static void loadConfigs(VexelCoreProxy VCP, Boolean reload) {
        long startTime = System.nanoTime();
        if (reload) {
            unloadFeatures(VCP);
        }
        VCP.toml = FileUtils.getTomlConfig(getDataDirectory(),"config.toml",instance);
        loadFeatures(VCP);
        long endTime = System.nanoTime();
        logger.info("All configs have been loaded! Took " + TimeUtils.convertDurationToMs(startTime,endTime));
    }

    public static void setCustomCommands(VexelCoreProxy VCP) {
        long startTime = System.nanoTime();
        List<HashMap<String, String>> data = VCP.toml.getList("customCommand");
        data.forEach((hashMap) -> {
            try {
                VelocityUtils.registerCommand(new String[] {hashMap.get("newCommand")},new CustomCommand(instance,hashMap),instance);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Failed to register custom command \"" + hashMap.get("newCommand") + "\"");
            }
            /*
            CommandMeta meta_cc = server.getCommandManager().metaBuilder(hashMap.get("newCommand")).build();
            server.getCommandManager().register(meta_cc, new CustomCommand(server, hashMap));

             */
        });
        long endTime = System.nanoTime();
        logger.info("Loaded " + data.size() + " custom command(s) into memory in " + TimeUtils.convertDurationToMs(startTime,endTime));
    }

    public static void unsetCustomCommands(VexelCoreProxy VCP) {
        List<HashMap<String, String>> data = VCP.toml.getList("customCommand");
        data.forEach((hashMap) -> {
            server.getCommandManager().unregister(hashMap.get("newCommand"));
            logger.debug("UNREGISTERED COMMAND: " + hashMap.get("newCommand"));
        });
    }

    public static void loadFeatures(VexelCoreProxy VCP) {
        if (VCP.toml.getTable("features").getBoolean("customCommand")) {
            setCustomCommands(VCP);
        }
        if (VCP.toml.getTable("features").getBoolean("customGlist")) {
            VelocityUtils.registerCommand(new String[] {"glist","temptest"},new Glist(instance),instance);
            // Remove "temptest" after making sure having just 1 alias works

            /*

            try {
                CommandMeta meta_glist = server.getCommandManager().metaBuilder("glist").build();
                server.getCommandManager().register(meta_glist,new Glist(server));
            } catch (Exception e) {
                e.printStackTrace();
                getLogger().error("Unable to enable custom glist due to an error!");
            }
             */
        }
    }

    public static void unloadFeatures(VexelCoreProxy VCP) {
        unsetCustomCommands(VCP);
        if (VCP.toml.getTable("features").getBoolean("customGlist")) {
            server.getCommandManager().unregister("glist");
        }
    }
}
