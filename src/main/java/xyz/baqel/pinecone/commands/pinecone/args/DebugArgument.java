package xyz.baqel.pinecone.commands.pinecone.args;

import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.commands.BaqelArgument;
import xyz.baqel.pinecone.data.PlayersData;
import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.Detection;
import xyz.baqel.pinecone.utils.BoxDebugType;
import xyz.baqel.pinecone.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugArgument extends BaqelArgument {
    public DebugArgument() {
        super("debug", "debug <bb, check, integrity> [args (check) (type)]", "toggle debug on/off for yourself.", "pinecone.debug");

        addTabComplete(2, "boundingBox");
        addTabComplete(2, "check");
        addTabComplete(2, "integrity");

        addTabComplete(3, "%check%,check,2");
        addTabComplete(4, "%detection%,check,2");

        addTabComplete(3, "hitbox,boundingbox,2");
        addTabComplete(3, "ground,boundingbox,2");
        addTabComplete(3, "collided,boundingbox,2");
        addTabComplete(3, "all,boundingbox,2");
        addTabComplete(3, "hitbox,bb,2");
        addTabComplete(3, "ground,bb,2");
        addTabComplete(3, "collided,bb,2");
        addTabComplete(3, "all,bb,2");
        addTabComplete(3, "hitbox,blockbox,2");
        addTabComplete(3, "ground,blockbox,2");
        addTabComplete(3, "collided,blockbox,2");
        addTabComplete(3, "all,blockbox,2");
    }

    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Pinecone.getInstance().getMessageFields().invalidArguments);
        }
        if (args[1].equalsIgnoreCase("check")) {
            if (args.length == 4 && sender instanceof Player) {
                Player player = (Player) sender;

                PlayersData data = Pinecone.getInstance().getDataManager().getPlayerData(player);

                Check check = Pinecone.getInstance().getCheckManager().getCheckByName(args[2]);
                if (check != null) {
                    Detection detection = check.getDetectionByName(args[3].replaceAll("_", " "));

                    if (detection != null) {
                        data.debugDetection = detection;
                        data.checkDebugEnabled = !data.checkDebugEnabled;
                        Pinecone.getInstance().debuggingPlayer = sender;
                        player.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.translate("&7Toggled debug for &f" + detection.getId() + "&7: &a" + data.checkDebugEnabled));
                    } else {
                        sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Red + "Invalid arguments. The detection '" + args[3] + "' does not exist.");
                    }
                } else {
                    sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Red + "Invalid arguments. The check '" + args[2] + "' does not exist.");
                }
                return;
            } else if (args.length == 5) {
                Player target = Bukkit.getPlayer(args[4]);
                if (target != null) {
                    PlayersData data = Pinecone.getInstance().getDataManager().getPlayerData(target);
                    Check check = Pinecone.getInstance().getCheckManager().getCheckByName(args[2]);
                    if (check != null) {
                        Detection detection = check.getDetectionByName(args[3].replaceAll("_", " "));

                        if (detection != null) {
                            Pinecone.getInstance().debuggingPlayer = sender;
                            data.debugDetection = detection;
                            data.checkDebugEnabled = !data.checkDebugEnabled;
                            sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix +
                                    Color.translate("&7Toggled debug for &f" + detection.getId() + "&7: &a" + data.checkDebugEnabled));
                        } else {
                            sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix +
                                    Color.Red + "Invalid arguments. the detection '" + args[3] + "' does not exist.");
                        }
                    } else {
                        sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix +
                                Color.Red + "Invalid arguments. The check '" + args[2] + "' does not exist.");
                    }
                } else {
                    sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix +
                            Color.Red + "The player '" + args[4] + "' is not online!");
                }
                return;
            }
            sender.sendMessage(Pinecone.getInstance().getMessageFields().invalidArguments);
            return;
        }
        if (args[1].equalsIgnoreCase("bb")
                || args[1].equalsIgnoreCase("boundingbox")
                || args[1].equalsIgnoreCase("blockbox")) {

            if (sender instanceof Player) {
                Player player = (Player) sender;
                PlayersData data = Pinecone.getInstance().getDataManager().getPlayerData(player);

                if (args.length == 3) {
                    if (BoxDebugType.isDebugType(args[2].toUpperCase())) {
                        BoxDebugType debugType = BoxDebugType.getFromString(args[2].toUpperCase());
                        data.boxDebugEnabled = !data.boxDebugEnabled;
                        data.boxDebugType = debugType;
                        sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix +
                                Color.Gray + "Set the " + debugType.toString() + " to " +
                                (data.boxDebugEnabled ? Color.Green + "true" : Color.Red + "false") + Color.Gray + ".");
                    } else {
                        sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix +
                                Color.Red + "Invalid arguments. Do /help for more information.");
                    }
                    return;
                }
            }
            if (args[1].equalsIgnoreCase("integrity") || args[1].equalsIgnoreCase("integ")) {
                if (args.length == 2) {
                    if (sender instanceof Player) {
                        PlayersData data = Pinecone.getInstance().getDataManager().getPlayerData((Player) sender);

                        if (data != null) {
                            data.integDebug = !data.integDebug;

                            sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix +
                                    Color.Gray + "Toggled integrity debug for yourself to: " + Color.White + data.integDebug);
                        } else {
                            sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix +
                                    Color.Red + "Error grabbing data object(s)!");
                        }
                    } else {
                        sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix +
                                Color.Red + "That player is not online!");
                    }
                }
                return;
            }
            sender.sendMessage(Pinecone.getInstance().getMessageFields().invalidArguments);
        }
    }
}
