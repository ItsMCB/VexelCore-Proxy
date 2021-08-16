package me.itsmcb.vexelcoreproxy.utils;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Arrays;

public class ChatUtils {

    public static TextComponent parseLegacy(String input) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(input);
    }

    public static TextComponent toComponent(String input) {
        return parseLegacy(input);
    }

    public static TextComponent toComponent(String input[]) {
        StringBuilder msg = new StringBuilder();
        Arrays.stream(input).forEach(textValue -> {
            msg.append(textValue + " ");
        });
        return parseLegacy(msg.toString());

    }

    public static TextComponent clickableComponent(String input, String hoverMsg, String action, String actionValue) {
        return ChatUtils.parseLegacy(input)
                .hoverEvent(HoverEvent.showText(ChatUtils.parseLegacy(hoverMsg)))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.valueOf(action),actionValue));
    }
}
