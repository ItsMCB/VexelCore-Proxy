package me.itsmcb.vexelcoreproxy.config.main;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class HelpOp {

    private Boolean enabled = true;
    private String usePermission = "vcp.helpop";
    private String seeHelpOpPermission = "vcp.seehelpop";

}
