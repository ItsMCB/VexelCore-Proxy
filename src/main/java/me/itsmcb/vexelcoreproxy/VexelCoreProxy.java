package me.itsmcb.vexelcoreproxy;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.commands.Glist;
import me.itsmcb.vexelcoreproxy.commands.MainCMD;
import me.itsmcb.vexelcoreproxy.extras.Metrics;
import me.itsmcb.vexelcoreproxy.features.CustomCommand;
import me.itsmcb.vexelcoreproxy.utils.ConfigUtils;
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

    private ProxyServer server;
    private Logger logger;
    private Path dataDirectory;
    private Toml toml;
    private VexelCoreProxy instance;
    private final Metrics.Factory metricsFactory;

    public Path getDataDirectory() { return dataDirectory; }
    public Toml getConfig() {
        return toml;
    }
    public void setConfig(Toml toml) {
        this.toml = toml;
    }
    public ProxyServer getProxyServer() { return server; }
    public Logger getLogger() { return logger; }

    @Inject
    public VexelCoreProxy(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
        this.instance = this;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            ConfigUtils.loadConfigs(instance,false);
            metricsFactory.make(this, 12763); // bStats
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("An error occurred while initializing VexelCore for Velocity. For the purpose of debugging, a shutdown has not been triggered.");
        }
    }
}
