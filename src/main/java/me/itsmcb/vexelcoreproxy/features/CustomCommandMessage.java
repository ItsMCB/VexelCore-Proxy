package me.itsmcb.vexelcoreproxy.features;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.config.main.cc.CCMDComponent;
import me.itsmcb.vexelcoreproxy.config.main.cc.CCMDExec;
import me.itsmcb.vexelcoreproxy.config.main.cc.CCMDMsg;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.ArrayList;
import java.util.List;

public class CustomCommandMessage implements SimpleCommand {

    private final CCMDMsg data;

    public CustomCommandMessage(CCMDMsg data) {
        this.data = data;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        ArrayList<TextComponent> tcFinal = new ArrayList<>();
        data.getComponents().forEach(ccmdComponent -> tcFinal.add(ChatUtils.getCCMessage(ccmdComponent)));
        tcFinal.forEach(source::sendMessage);
    }
}
