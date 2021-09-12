package dev.chrix.moon;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class Ping {
    /**
     * Message command for "+ping", calculates current ping status of bot
     *
     * @param event Received event
     */
    public static void handleEvent(MessageReceivedEvent event) {
        // Require kick permissions to call command
        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
            // Takes text channel command was called in
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();

            // Sends response message
            channel.sendMessage("Pong!")
                    .queue(response /* => Message */ -> {
                        // Edits with ping response time of bot
                        response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                    });
        }
    }

    /**
     * Slash command of "+ping"
     *
     * @param event Received slash event
     */
    public static void handleSlash(SlashCommandEvent event) {
        // Require kick permissions to call command
        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
            long time = System.currentTimeMillis();
            // Sends reply to user only, not text channel where slash command was sent
            event.reply("Pong!").setEphemeral(true)
                    .flatMap(v ->
                            event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                    ).queue(); // Queues both reply and edit
        }
    }


}
