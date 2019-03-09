package xyz.baqel.pinecone.commands.pinecone.args;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.commands.BaqelArgument;
import xyz.baqel.pinecone.data.PlayersData;
import xyz.baqel.pinecone.utils.Color;
import xyz.baqel.pinecone.utils.MathUtils;
import xyz.baqel.pinecone.utils.MiscUtils;

public class ViewArgument extends BaqelArgument {
    public ViewArgument() {
        super("view", "view <player>", "view a player's data collected by Pinecone.", "pinecone.violations");
    }

    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Color.Red + "That player is not online!");
                return;
            }
            PlayersData data = Pinecone.getInstance().getDataManager().getPlayerData(target);
            if (data == null) {
                sender.sendMessage(Color.Red + "Unknown error occurred that prevented Pinecone from accessing the target's data.");
                return;
            }
            sender.sendMessage(MiscUtils.line(Color.Dark_Gray));
            sender.sendMessage(Color.Green + Color.Bold + target.getName() + "'s Information");
            sender.sendMessage("");
            sender.sendMessage(Color.Green + "Ping: " + Color.White + data.ping);
            sender.sendMessage(Color.Green + "Reliability: " + Color.White + data.reliabilityPercentage + "%");
            sender.sendMessage("");
            sender.sendMessage(Color.Green + "Violations:");
            if (!Pinecone.getInstance().getCheckManager().violations.containsKey(data.player.getUniqueId())) {
                sender.sendMessage(Color.Gray + "No violations.");
            } else {
                Pinecone.getInstance().getCheckManager().violations.get(data.player.getUniqueId()).forEach(vl -> {
                    sender.sendMessage(Color.translate("&8>> &a" + vl.getCheck().getName() + " &7[&c" + MathUtils.trim(2, vl.getCombinedAmount()) + "&7]"));

                    vl.getSpecificViolations().keySet().forEach(detection -> {
                        sender.sendMessage(Color.translate("&8- &b" + detection.getId() + " &7[" + vl.getSpecificViolations().get(detection) + "&7]"));
                    });
                });
            }
            sender.sendMessage(MiscUtils.line(Color.Dark_Gray));
        } else {
            sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Pinecone.getInstance().getMessageFields().invalidArguments);
        }
    }
}
