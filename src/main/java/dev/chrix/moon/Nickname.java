package dev.chrix.moon;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Nickname {

    /**
     * Message command for "+nick", changes nick based on requested params
     *
     * @param event Received event
     */
    public static void handleEvent(MessageReceivedEvent event) {
        // Require kick permissions to call command
        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
            // Takes message parameters and channel where command was called
            Message msg = event.getMessage();
            MessageChannel channel = event.getChannel();
            try {
                // Changes user's nickname
                event.getMember().modifyNickname(msg.getContentRaw().substring(6)).queue();
                //.reason(event.getMember().getUser().getName() + " changed their nickname to " + event.getMember().getNickname());

                // Cleans up chat
                msg.delete().queue();
                // Announces to user it sucessfully changed nickname
                channel.sendMessage("Nickname has been successfully changed!")
                        // Cleans up chat
                        .delay(5, TimeUnit.SECONDS)
                        .flatMap(Message::delete)
                        .queue();
            } catch (IndexOutOfBoundsException e) {
                // Announces misuse of command
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

}
