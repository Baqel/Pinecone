package xyz.baqel.pinecone.commands.pinecone.args;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.commands.BaqelArgument;
import xyz.baqel.pinecone.data.PlayersData;
import xyz.baqel.pinecone.utils.Color;

public class AlertsArgument extends BaqelArgument {
    public AlertsArgument() {
        super("alerts", "alerts", "Toggles alerts on and off.", "pinecone.alerts");

        addTabComplete(2, "true");
        addTabComplete(2, "false");
    }

    @Override
    public void onArgument(CommandSender sender, Command command, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayersData data = Pinecone.getInstance().getDataManager().getPlayerData(player);
            if (data == null) {
                player.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Red + "Unknown error occured where your data object returns null.");
                return;
            }
            data.alerts = !data.alerts;
            player.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Gray + "Set your alerts mode to " + (data.alerts ? Color.Green + "true" : Color.Red + "false") + Color.Gray + "!");
        } else {
            sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Red + "You must be a player to use this command!");
        }
    }

}
