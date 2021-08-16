package me.itsmcb.vexelcoreproxy;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.commands.CustomCommand;
import me.itsmcb.vexelcoreproxy.commands.Glist;
import me.itsmcb.vexelcoreproxy.commands.MainCMD;
import me.itsmcb.vexelcoreproxy.utils.FileUtils;
import me.itsmcb.vexelcoreproxy.utils.TimeUtils;
import me.itsmcb.vexelcoreproxy.utils.VelocityUtils;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("An error occurred while initializing VexelCore for Velocity. For the purpose of debugging, a shutdown has not been triggered.");
        }
    }
    // Display enabled features and custom command output as hoverable text added onto the /vcp output
    // Option for CC's to only run on particular servers ex. onlyWorkOn arg
    // Tab completion for commands




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
        AtomicInteger amountOfRegisteredCC = new AtomicInteger();
        instance.toml.getTables("customCommand").forEach(data -> {
            try {
                VelocityUtils.registerCommand(new String[] {data.getString("newCommand")},new CustomCommand(instance,data),instance);
                amountOfRegisteredCC.getAndIncrement();
            } catch (Exception e) {
                logger.warn("Failed to register custom command \"" + data.getString("newCommand") + "\"");
            }
        });
        long endTime = System.nanoTime();
        logger.info("Loaded " + amountOfRegisteredCC + " custom command(s) into memory in " + TimeUtils.convertDurationToMs(startTime,endTime));
    }

    public static void unsetCustomCommands(VexelCoreProxy VCP) {
        List<HashMap<String, String>> data = VCP.toml.getList("customCommand");
        data.forEach((hashMap) -> {
            server.getCommandManager().unregister(hashMap.get("newCommand"));
        });
    }

    public static void loadFeatures(VexelCoreProxy VCP) {
        if (VCP.toml.getTable("features").getBoolean("vcp-main")) {
            VelocityUtils.registerCommand(new String[] {"vcp","vexelcoreproxy"},new MainCMD(instance),instance);
        }
        if (VCP.toml.getTable("features").getBoolean("customCommand")) {
            setCustomCommands(VCP);
        }
        if (VCP.toml.getTable("features").getBoolean("customGlist")) {
            VelocityUtils.registerCommand(new String[] {"glist"},new Glist(instance),instance);
        }
    }

    public static void unloadFeatures(VexelCoreProxy VCP) {
        if (VCP.toml.getTable("features").getBoolean("vcp-main")) {
            server.getCommandManager().unregister("vcp");
        }
        if (VCP.toml.getTable("features").getBoolean("customCommand")) {
            unsetCustomCommands(VCP);
        }
        if (VCP.toml.getTable("features").getBoolean("customGlist")) {
            server.getCommandManager().unregister("glist");
        }
    }
}
