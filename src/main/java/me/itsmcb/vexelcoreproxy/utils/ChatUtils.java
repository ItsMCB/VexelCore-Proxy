package me.itsmcb.vexelcoreproxy.utils;

import com.moandjiezana.toml.Toml;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
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
        if (hoverMsg == null) {
            return ChatUtils.parseLegacy(input)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.valueOf(action),actionValue));
        }
        return ChatUtils.parseLegacy(input)
                .hoverEvent(HoverEvent.showText(ChatUtils.parseLegacy(hoverMsg)))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.valueOf(action),actionValue));
    }

    public static ArrayList<TextComponent> sendCCMessage(Toml data) {
        ArrayList<TextComponent> tcList = new ArrayList<>();
        data.getTables("components").forEach(msgData -> {
            String action = msgData.getString("action");
            if (action != null) {
                if (action.equalsIgnoreCase("open_url") || action.equalsIgnoreCase("run_command") || action.equalsIgnoreCase("suggest_command") || action.equalsIgnoreCase("copy_to_clipboard"))
                    tcList.add(ChatUtils.clickableComponent(
                            msgData.getString("content"),
                            msgData.getString("hover"),
                            action,
                            msgData.getString("actionValue")));
            } else {
                tcList.add(ChatUtils.parseLegacy(msgData.getString("content")));
            }
        });
        return tcList;
    }
}
