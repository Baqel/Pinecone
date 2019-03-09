package xyz.baqel.pinecone.commands.pinecone.args;

import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.commands.BaqelArgument;
import xyz.baqel.pinecone.data.logging.Yaml;
import xyz.baqel.pinecone.utils.Color;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SaveArgument extends BaqelArgument {
    public SaveArgument() {
        super("savelogs", "savelogs", "force-save the logs of player's to a Yaml file.", "pinecone.save");

        addAlias("forcesave");
        addAlias("save");
        addAlias("sl");
        addAlias("slogs");
    }

    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        if (Pinecone.getInstance().getDataManager().getLogger() instanceof Yaml) {
            sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Red + "Saving all violation logs...");
            Yaml logger = (Yaml) Pinecone.getInstance().getDataManager().getLogger();

            logger.dumpLogs();
            sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Green + "Saved!");
        } else {
            sender.sendMessage(ChatColor.RED + "This feature is only for YAML logging since it is useless in other forms.");
        }
    }
}
