package me.itsmcb.vexelcoreproxy.utils;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class MessageUtils {
    public static Toml language = VexelCoreProxy.getConfig().getTable("language");

    public static TextComponent get(String key) {
        return ChatUtils.parseLegacy(language.getString(key));
    }

    public static TextComponent getWithPrefix(String key) { return ChatUtils.parseLegacy(VexelCoreProxy.getConfig().getString("prefix") + language.getString(key)); }

    public static void sendUsage(CommandSource source, String usage) {
        source.sendMessage(MessageUtils.get("invalidUsage").append(Component.text(usage)));
    }
}
