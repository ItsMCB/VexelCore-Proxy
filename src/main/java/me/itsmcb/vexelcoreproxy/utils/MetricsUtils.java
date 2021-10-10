package me.itsmcb.vexelcoreproxy.utils;

import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.extras.Metrics;

public class MetricsUtils {

    public static void registerMetrics(VexelCoreProxy instance, int serviceId, Metrics.Factory metricsFactory) {
        Metrics metrics = metricsFactory.make(instance, serviceId);
        metrics.addCustomChart(new Metrics.SingleLineChart("custom_command_execute_amount", () -> ConfigUtils.getExecuteCCAmount(instance)));
        metrics.addCustomChart(new Metrics.SingleLineChart("custom_command_message_amount", () -> ConfigUtils.getMessageCCAmount(instance)));
    }
}
