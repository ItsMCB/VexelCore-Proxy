package me.itsmcb.vexelcoreproxy.commands;

import com.google.common.io.BaseEncoding;
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
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.List;

public class PlayerInformation implements SimpleCommand {

    private final ProxyServer server;
    private final CommentedConfigurationNode config;
    private final CommentedConfigurationNode language;

    public PlayerInformation(VexelCoreProxy VCP) {
        this.server = VCP.getProxyServer();
        this.config = VCP.getYamlConfig().get();
        this.language = VCP.getLang().get();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (!source.hasPermission(config.node("player-information").node("permission").getString())) {
            ChatUtils.sendMsg(source, language.node("general").node("prefix").getString(), language.node("error").node("noPermission").getString());
            return;
        }
        if (args.length > 0) {
            Player targetPlayer = VelocityUtils.getPlayer(server, args[0]);
            if (targetPlayer != null) {
                ChatUtils.sendMsg(source, language.node("player-info").node("connectionProfileHeader").getString().replace("[playerName]", targetPlayer.getUsername()));
                sendUserInfo(source, targetPlayer);
                sendServerInfo(source, targetPlayer);
                sendResourcePackInfo(source,targetPlayer);
            } else {
                ChatUtils.sendMsg(source, language.node("player-info").node("playerNotOnline").getString());
            }
            return;
        }
        ChatUtils.sendMsg(source, language.node("error").node("invalidUsage").getString(), "/playerinformation <player name>");
    }

    public void sendResourcePackInfo(CommandSource source, Player targetPlayer) {
        ResourcePackInfo rpInfo = targetPlayer.getAppliedResourcePack();
        TextComponent hover = ChatUtils.parseLegacy(language.node("player-info").node("playerResourcePackNotFound").getString());
        TextComponent component = ChatUtils.parseLegacy(language.node("player-info").node("pinfoRPL0").getString());
        try {
            hover = ChatUtils.parseLegacy(
                    language.node("player-info").node("pinfoRPL1").getString(),
                    rpInfo.getUrl(),
                    "\n" + language.node("player-info").node("pinfoRPL2").getString(),
                    BaseEncoding.base16().lowerCase().encode(rpInfo.getHash()),
                    "\n" + language.node("player-info").node("pinfoRPL3").getString(),
                    rpInfo.getOrigin().name().replace("_"," ").toLowerCase(),
                    "\n" + language.node("general").node("bar").getString(),
                    "\n" + language.node("player-info").node("pinfoRPL4").getString());
            source.sendMessage(component.hoverEvent(HoverEvent.showText(hover)).clickEvent(ClickEvent.openUrl(rpInfo.getUrl())));
        } catch (Exception e) {
            source.sendMessage(component.hoverEvent(HoverEvent.showText(hover)));
        }
    }

    public void sendUserInfo(CommandSource source, Player targetPlayer) {
        TextComponent hover = ChatUtils.parseLegacy(
                language.node("player-info").node("pinfoUserL1").getString(), targetPlayer.getUsername(),
                "\n" + language.node("player-info").node("pinfoUserL2").getString(), targetPlayer.getEffectiveLocale()+"",
                "\n" + language.node("player-info").node("pinfoUserL3").getString(), targetPlayer.getUniqueId()+"",
                "\n" + language.node("player-info").node("pinfoUserL4").getString(), targetPlayer.getClientBrand() + " " +targetPlayer.getProtocolVersion().getMostRecentSupportedVersion() + " &7(" + targetPlayer.getProtocolVersion().getProtocol() + ")",
                "\n" + language.node("player-info").node("pinfoUserL5").getString(), targetPlayer.getPlayerSettings().getViewDistance()+"",
                "\n" + language.node("player-info").node("pinfoUserL6").getString(), targetPlayer.getPlayerSettings().getChatMode().name(),
                "\n" + language.node("player-info").node("pinfoUserL7").getString(),  targetPlayer.getRemoteAddress().getHostString() + ":" +targetPlayer.getRemoteAddress().getPort(),
                "\n" + language.node("general").node("bar").getString(),
                "\n" + language.node("player-info").node("pinfoUserL8").getString());
        TextComponent component = ChatUtils.parseLegacy(language.node("player-info").node("pinfoUserL0").getString()).hoverEvent(HoverEvent.showText(hover)).clickEvent(ClickEvent.suggestCommand(targetPlayer.getUniqueId() + ""));
        source.sendMessage(component);
    }

    public void sendServerInfo(CommandSource source, Player targetPlayer) {
        ServerInfo serverInfo = targetPlayer.getCurrentServer().get().getServerInfo();
        TextComponent hover = ChatUtils.parseLegacy(
                language.node("player-info").node("pinfoServerL1").getString(), serverInfo.getName(),
                "\n" + language.node("player-info").node("pinfoServerL2").getString(), targetPlayer.getPing()+"",
                "\n" + language.node("general").node("bar").getString(),
                "\n" + language.node("player-info").node("pinfoServerL3").getString());
        TextComponent component = ChatUtils.parseLegacy(language.node("player-info").node("pinfoServerL0").getString()).hoverEvent(HoverEvent.showText(hover)).clickEvent(ClickEvent.runCommand("/server " + serverInfo.getName()));
        source.sendMessage(component);
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return TabUtils.returnTab(invocation.arguments(), TabUtils.getAllPlayers(server));
    }
}
