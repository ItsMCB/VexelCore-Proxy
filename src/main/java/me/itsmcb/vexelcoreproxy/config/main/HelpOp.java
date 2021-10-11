package me.itsmcb.vexelcoreproxy.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class HelpOp {

    private Boolean enabled = true;
    private String usePermission = "vcp.helpop";
    private String seeHelpOpPermission = "vcp.seehelpop";
    private List<String> aliases = List.of("helpop");

}
