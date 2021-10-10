package me.itsmcb.vexelcoreproxy.config.main.cc;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class CCMDMsg {

    private String newCommand;
    private List<CCMDComponent> components;

    public CCMDMsg() {}
    public CCMDMsg(String newCommand, List<CCMDComponent> components) { // Message
        this.newCommand = newCommand;
        this.components = components;
    }

    public String getNewCommand() {
        return this.newCommand;
    }
    public List<CCMDComponent> getComponents() {
        return components;
    }
}
