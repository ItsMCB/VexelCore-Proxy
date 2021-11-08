package me.itsmcb.vexelcoreproxy.config.main;

import me.itsmcb.vexelcoreproxy.config.main.cc.CustomCommand;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class MainConfig {

    private String language = "en_US";

    Main main;

    Glist glist;

    Jump jump;

    Broadcast broadcast;

    PlayerInformation playerInformation;

    HelpOp helpop;

    CustomCommand customCommands;

    Report report;

}

