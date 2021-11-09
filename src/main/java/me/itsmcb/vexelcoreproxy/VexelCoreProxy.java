package me.itsmcb.vexelcoreproxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.config.VCPConfig;
import me.itsmcb.vexelcoreproxy.features.FeatureHandler;
import me.itsmcb.vexelcoreproxy.extras.Metrics;
import me.itsmcb.vexelcoreproxy.utils.ConfigUtils;
import me.itsmcb.vexelcoreproxy.utils.MetricsUtils;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "vexelcore",
        name = "VexelCoreProxy",
        version = "@version@",
        description = "A modular and lightweight essential features plugin for the Velocity Minecraft proxy.",
        url = "https://github.com/Vexelosity/VexelCore-Proxy",
        authors = {"ItsMCB"}
)
public class VexelCoreProxy {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private final VexelCoreProxy instance;
    private final Metrics.Factory metricsFactory;
    private VCPConfig mainConfig;
    private VCPConfig language;
    private FeatureHandler featureHandler;

    public Path getDataDirectory() { return dataDirectory; }
    public VCPConfig getConfig() {
        return mainConfig;
    }
    public VCPConfig getLang() {
        return language;
    }
    public ProxyServer getProxyServer() { return server; }
    public Logger getLogger() { return logger; }
    public FeatureHandler getFeatureHandler() { return featureHandler; }

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
            // Register metrics
            MetricsUtils.registerMetrics(this, 12763, metricsFactory);

            // Generate or load main config file
            this.mainConfig = new VCPConfig(instance, dataDirectory, "config");

            // Generate language files
            new VCPConfig(instance, dataDirectory, "language/en_US");

            // Set language data
            this.language = new VCPConfig(instance, dataDirectory, "language/" + getConfig().get().node("language").getString());

            // Enable features
            this.featureHandler = new FeatureHandler(instance);
            ConfigUtils.loadFeatures(instance);

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("An error occurred while initializing VexelCore for Velocity.");
        }
    }
}
