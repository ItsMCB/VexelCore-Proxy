package me.itsmcb.vexelcoreproxy.utils;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import me.itsmcb.vexelcoreproxy.VexelCoreProxy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.Arrays;

public class MessageUtils {
    /*
    public static Toml language = VexelCoreProxy.getConfig().getTable("language");

    public static TextComponent get(String key) {
        return ChatUtils.parseLegacy(language.getString(key));
    }

    public static TextComponent getWithPrefix(String key) { return ChatUtils.parseLegacy(VexelCoreProxy.getConfig().getString("prefix") + language.getString(key)); }

    public static void sendUsage(CommandSource source, String usage) {
        source.sendMessage(MessageUtils.get("invalidUsage").append(Component.text(usage)));
    }
     */
    public static TextComponent toComponent(String input) {
        return ChatUtils.parseLegacy(input);
    }
    public static TextComponent toComponent(String input[]) {
        StringBuilder msg = new StringBuilder();
        Arrays.stream(input).forEach(textValue -> {
            msg.append(textValue + " ");
        });
        return ChatUtils.parseLegacy(msg.toString());

    }
}
