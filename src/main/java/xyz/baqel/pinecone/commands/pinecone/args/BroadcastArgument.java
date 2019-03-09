package xyz.baqel.pinecone.commands.pinecone.args;

import xyz.baqel.pinecone.commands.BaqelArgument;
import xyz.baqel.pinecone.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BroadcastArgument extends BaqelArgument {
    public BroadcastArgument() {
        super("broadcast", "broadcast <message>", "broadcast a message to the entire server.", "pinecone.broadcast");
    }

    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        StringBuilder message = new StringBuilder();
        for (int i = 1; i != args.length; ++i) {
            message.append(args[i]).append(" ");
        }
        Bukkit.broadcastMessage(Color.translate(message.toString()));
    }
}
