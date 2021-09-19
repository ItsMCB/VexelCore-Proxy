package me.itsmcb.vexelcoreproxy.commands;

import com.google.common.io.BaseEncoding;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.player.ResourcePackInfo;
import com.velocitypowered.api.proxy.server.ServerInfo;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import me.itsmcb.vexelcoreproxy.utils.ChatUtils;
import me.itsmcb.vexelcoreproxy.utils.TabUtils;
import me.itsmcb.vexelcoreproxy.utils.VelocityUtils;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.List;

public class PlayerInformation implements SimpleCommand {

    private final ProxyServer server;
    private final Toml config;
    private final Toml language;

    public PlayerInformation(VexelCoreProxy VCP) {
        this.server = VCP.getProxyServer();
        this.config = VCP.getConfig();
        this.language = config.getTable("language");
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (!source.hasPermission(config.getTable("permissions").getString("playerInformation"))) {
            ChatUtils.sendMsg(source, config.getString("prefix"), language.getString("noPermission"));
            return;
        }
        Player targetPlayer = VelocityUtils.getPlayer(server, args[0]);
        if (targetPlayer != null) {
            ChatUtils.sendMsg(source, language.getString("connectionProfileHeader").replace("%player%", targetPlayer.getUsername()));
            sendUserInfo(source, targetPlayer);
            sendServerInfo(source, targetPlayer);
            sendResourcePackInfo(source,targetPlayer);
        } else {
            ChatUtils.sendMsg(source, language.getString("playerNotOnline"));
        }
    }

    public void sendResourcePackInfo(CommandSource source, Player targetPlayer) {
        ResourcePackInfo rpInfo = targetPlayer.getAppliedResourcePack();
        TextComponent hover = ChatUtils.parseLegacy(language.getString("playerResourcePackNotFound"));
        TextComponent component = ChatUtils.parseLegacy(language.getString("pinfoRPL0"));
        try {
            hover = ChatUtils.parseLegacy(
                    language.getString("pinfoRPL1"),
                    rpInfo.getUrl(),
                    "\n" + language.getString("pinfoRPL2"),
                    BaseEncoding.base16().lowerCase().encode(rpInfo.getHash()),
                    "\n" + language.getString("pinfoRPL3"),
                    rpInfo.getOrigin().name().replace("_"," ").toLowerCase(),
                    "\n" + language.getString("bar"),
                    "\n" + language.getString("pinfoRPL4"));
            source.sendMessage(component.hoverEvent(HoverEvent.showText(hover)).clickEvent(ClickEvent.openUrl(rpInfo.getUrl())));
        } catch (Exception e) {
            source.sendMessage(component.hoverEvent(HoverEvent.showText(hover)));
        }
    }

    public void sendUserInfo(CommandSource source, Player targetPlayer) {
        TextComponent hover = ChatUtils.parseLegacy(
                language.getString("pinfoUserL1"), targetPlayer.getUsername(),
                "\n" + language.getString("pinfoUserL2"), targetPlayer.getEffectiveLocale()+"",
                "\n" + language.getString("pinfoUserL3"), targetPlayer.getUniqueId()+"",
                "\n" + language.getString("pinfoUserL4"), targetPlayer.getClientBrand() + " " +targetPlayer.getProtocolVersion().getMostRecentSupportedVersion() + " &7(" + targetPlayer.getProtocolVersion().getProtocol() + ")",
                "\n" + language.getString("pinfoUserL5"), targetPlayer.getPlayerSettings().getViewDistance()+"",
                "\n" + language.getString("pinfoUserL6"), targetPlayer.getPlayerSettings().getChatMode().name(),
                "\n" + language.getString("pinfoUserL7"),  targetPlayer.getRemoteAddress().getHostString() + ":" +targetPlayer.getRemoteAddress().getPort(),
                "\n" + language.getString("bar"),
                "\n" + language.getString("pinfoUserL8"));
        TextComponent component = ChatUtils.parseLegacy(language.getString("pinfoUserL0")).hoverEvent(HoverEvent.showText(hover)).clickEvent(ClickEvent.suggestCommand(targetPlayer.getUniqueId() + ""));
        source.sendMessage(component);
    }


    public void sendServerInfo(CommandSource source, Player targetPlayer) {
        ServerInfo serverInfo = targetPlayer.getCurrentServer().get().getServerInfo();
        TextComponent hover = ChatUtils.parseLegacy(
                language.getString("pinfoServerL1"), serverInfo.getName(),
                "\n" + language.getString("pinfoServerL2"), targetPlayer.getPing()+"",
                "\n" + language.getString("bar"),
                "\n" + language.getString("pinfoServerL3"));
        TextComponent component = ChatUtils.parseLegacy(language.getString("pinfoServerL0")).hoverEvent(HoverEvent.showText(hover)).clickEvent(ClickEvent.runCommand("/server " + serverInfo.getName()));
        source.sendMessage(component);
    }
    @Override
    public List<String> suggest(Invocation invocation) {
        return TabUtils.returnTab(invocation.arguments(), TabUtils.getAllPlayers(server));
    }
}
