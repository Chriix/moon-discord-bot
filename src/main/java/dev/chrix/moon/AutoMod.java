package dev.chrix.moon;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import okhttp3.EventListener;
import com.google.re2j.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;


public class AutoMod extends EventListener {
    private final ArrayList<Pattern> filters;
    private boolean commandToggle;

    public AutoMod() throws IOException {
        filters = new ArrayList<>(50);
        commandToggle = true;
        BufferedReader scan = new BufferedReader(
                new FileReader(new File("E:\\Projects\\moon\\src\\main\\java\\dev\\chrix\\moon\\filters.txt")));

        String str;
        while ((str = scan.readLine()) != null) {
            // Adds filters to list to be stored for checking
            filters.add(Pattern.compile(str, Pattern.CASE_INSENSITIVE));
        }
    }
    /**
     * Message command for "+AutoMod", enables/disables automod
     *
     * @param event Received event
     */
    public void handleEvent(MessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            Message message = event.getMessage();
            MessageChannel channel = event.getChannel();
            if (message.getContentRaw().toLowerCase().startsWith(Moon.getPrefix() + "automod")) {
                switch (message.getContentRaw().substring(8).replaceAll(" ", "").toLowerCase()) {
                    case "on":
                        commandToggle = true;
                        channel.sendMessage("AutoMod has been enabled!").queue();
                        break;
                    case "off":
                        commandToggle = false;
                        channel.sendMessage("AutoMod has been disabled.").queue();
                        break;
                    default:
                        channel.sendMessage("Enable AutoMod with `+automod on`\nDisable AutoMod with `+automod off`").queue();
                }
            }
        }
    }

    /**
     * Checks filter list for detection and deletes message if found true
     * @param event Received message
     */
    public void handleMessage(MessageReceivedEvent event) {
        if (commandToggle) {
            Message message = event.getMessage();

            for (Pattern pattern : filters) {
                if (pattern.matcher(message.getContentRaw()).find()) {
                    event.getMessage().delete().queue();
                }
            }
        }
    }
}
