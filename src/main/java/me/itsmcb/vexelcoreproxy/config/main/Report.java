package me.itsmcb.vexelcoreproxy.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class Report {

    private Boolean enabled = true;
    private String permission = "vcp.report";
    private String seeReportPermission = "vcp.seereport";
    private List<String> aliases = List.of("report");

}