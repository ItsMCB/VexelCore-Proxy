package me.itsmcb.vexelcoreproxy.utils;

import com.velocitypowered.api.command.CommandSource;
import me.itsmcb.vexelcoreproxy.config.main.cc.CCMDComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ChatUtils {

    public static TextComponent parseLegacy(String... input) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(String.join(" ", input));
    }

    public static TextComponent toComponent(String ...input) {
        return parseLegacy(String.join(" ", input));

    }

    public static TextComponent clickableComponent(String input, String hoverMsg, String action, String actionValue) {
        if (hoverMsg == null) {
            return ChatUtils.parseLegacy(input)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.valueOf(action),actionValue));
        }
        return ChatUtils.parseLegacy(input)
                .hoverEvent(HoverEvent.showText(ChatUtils.parseLegacy(hoverMsg)))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.valueOf(action),actionValue));
    }

    public static TextComponent getCCMessage(CCMDComponent data) {
        String action = data.action();
        if (action != null) {
            if (action.equalsIgnoreCase("open_url") || action.equalsIgnoreCase("run_command") || action.equalsIgnoreCase("suggest_command") || action.equalsIgnoreCase("copy_to_clipboard"))
                return ChatUtils.clickableComponent(
                        data.content(),
                        data.hover(),
                        action,
                        data.actionValue());
        } else {
            return ChatUtils.parseLegacy(data.content());
        }
        return null;
    }

    public static void sendMsg(CommandSource source, String... input) {
        source.sendMessage(parseLegacy(String.join(" ", input)));
    }

    public static void sendComponentMsg(CommandSource source, String input, TextComponent textComponent) {
        source.sendMessage(parseLegacy(input + " ").append(textComponent));
    }

}
