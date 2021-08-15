package me.itsmcb.vexelcoreproxy.utils;

import net.kyori.adventure.text.TextComponent;

import java.util.Arrays;

public class MessageUtils {

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
