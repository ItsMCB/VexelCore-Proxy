package me.itsmcb.vexelcoreproxy.utils;

import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.extras.Metrics;

import java.util.HashMap;
import java.util.Map;

public class MetricsUtils {

    public static void registerMetrics(VexelCoreProxy instance, int serviceId, Metrics.Factory metricsFactory) {
        Metrics metrics = metricsFactory.make(instance, serviceId);
        metrics.addCustomChart(new Metrics.DrilldownPie("amount_of_custom_commands", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            String amountOfCCs = ConfigUtils.getAmountOfCustomCommands(instance) + ""; // Apparently must be a String
            Map<String, Integer> entry = new HashMap<>();
            entry.put(amountOfCCs, 1);
            map.put(amountOfCCs, entry);
            return map;
        }));
    }
}
