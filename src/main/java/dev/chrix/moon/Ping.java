package dev.chrix.moon;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class Ping {

    public static void handleEvent(MessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!") /* => RestAction<Message> */
                    .queue(response /* => Message */ -> {
                        response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                    });
        }
    }

    public static void handleSlash(SlashCommandEvent event) {
        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
            long time = System.currentTimeMillis();
            event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                    .flatMap(v ->
                            event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                    ).queue(); // Queue both reply and edit
        }
    }


    public static String getDescription() {
        return null;
    }


}
