package xyz.baqel.pinecone.commands.pinecone.args;

import xyz.baqel.pinecone.commands.BaqelArgument;
import xyz.baqel.pinecone.utils.Color;
import xyz.baqel.pinecone.utils.MiscUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AutobanArgument extends BaqelArgument {
    public AutobanArgument() {
        super("autoban", "autoban <player> <time>", "set a player to be autobanned.", "pinecone.autoban");

        addTabComplete(3, "45s/1m/1m30s");
    }

    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        MiscUtils.send(sender, Color.translate("&cThis command is in development!"));
    }
}
