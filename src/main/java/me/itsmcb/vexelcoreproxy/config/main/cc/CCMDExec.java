package me.itsmcb.vexelcoreproxy.config.main.cc;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class CCMDExec {

    private String newCommand;
    private List<String> executions;

    public CCMDExec() {}
    public CCMDExec(String newCommand, List<String> executions) {
        this.newCommand = newCommand;
        this.executions = executions;
    }

    public String getNewCommand() {
        return this.newCommand;
    }
    public List<String> getExecutions() {
        return this.executions;
    }

}
