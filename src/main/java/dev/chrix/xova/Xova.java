package dev.chrix.xova;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Objects;

public class Xova extends ListenerAdapter {

    private String botPrefix = "+";

    public static void main(String[] args) throws Exception {

        JDA api = JDABuilder.createLight(Secret.BOT_TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new Xova())
                .setActivity(Activity.playing("Chrix suffering!"))
                .build();

        api.upsertCommand("ping", "Calculates the ping of Xova").queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel channel = event.getChannel();
        // Checks for message sender and bot prefix
        if (event.getAuthor().isBot() || !msg.getContentRaw().startsWith(botPrefix)) return;
        switch (msg.getContentRaw().substring(1).toLowerCase().split(" ")[0]) {
            case "ping":
                Ping.handleEvent(event);
                break;
            case "prefix":
                changePrefix(event);
                break;
            default:
                channel.sendMessage("That command does not exist. Please see `+help` for a list of commands.").queue();
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.getName().equals("ping")) return; // make sure we handle the right command
        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                ).queue(); // Queue both reply and edit
    }

    private void changePrefix(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel channel = event.getChannel();
        if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            try {
                String newPrefix = msg.getContentRaw().substring(8);
                if (newPrefix.length() != 1) {
                    channel.sendMessage("Prefix must be exactly 1 character.\nFor example: `+prefix !`").queue();
                } else {
                    botPrefix = newPrefix;
                    channel.sendMessage("Prefix has been successfully updated!").queue();
                }
            } catch (StringIndexOutOfBoundsException e) {
                channel.sendMessage("Prefix is used to change the command prefix of Xova.\nCommand usage: `+prefix !`").queue();
                //e.printStackTrace();
            }
        }
    }
}


