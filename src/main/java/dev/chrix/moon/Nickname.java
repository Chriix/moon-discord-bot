package dev.chrix.moon;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Nickname {
    public static void handleEvent(MessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
            Message msg = event.getMessage();
            MessageChannel channel = event.getChannel();
            try {
                event.getMember().modifyNickname(msg.getContentRaw().substring(6)).queue();
                //.reason(event.getMember().getUser().getName() + " changed their nickname to " + event.getMember().getNickname());
                msg.delete().queue();
                channel.sendMessage("Nickname has been successfully changed!")
                        .delay(5, TimeUnit.SECONDS)
                        .flatMap(Message::delete)
                        .queue();
            } catch (IndexOutOfBoundsException e) {
                channel.sendMessage("Nick is used to change your nickname.\nCommand usage: `+nick Xova`").queue();
            }
        }
    }

//    public static void handleSlash(SlashCommandEvent event) {
//        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_WRITE)) {
//            try {
//                event.getMember().modifyNickname(event.getSubcommandGroup()).queue(); // Broken
//                event.reply("Successfully updated nickname!").setEphemeral(true).queue();
//            } catch (HierarchyException e) {
//                event.reply("Cannot change nickname of user with moderator permissions.").setEphemeral(true).queue();
//            }
//        }
//    }

    public static String getDescription() {
        return null;
    }
}
