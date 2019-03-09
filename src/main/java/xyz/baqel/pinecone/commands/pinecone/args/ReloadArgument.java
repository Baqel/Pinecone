package xyz.baqel.pinecone.commands.pinecone.args;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.commands.BaqelArgument;
import xyz.baqel.pinecone.utils.Color;

public class ReloadArgument extends BaqelArgument {
    public ReloadArgument() {
        super("reload", "reload <full/config/data>", "reload different parts of Pinecone.", "pinecone.reload");

        addTabComplete(2, "full");
        addTabComplete(2, "partial");
        addTabComplete(2, "config");
        addTabComplete(2, "data");
    }

    @Override
    public void onArgument(CommandSender sender, Command command, String[] args) {
        if (args.length == 2) {
            sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Gray + "Working...");
            switch (args[1]) {
                case "full": {
                    sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Gray + "Reloading configurations...");
                    Pinecone.getInstance().reloadConfig();
                    Pinecone.getInstance().reloadConfigObject();
                    Pinecone.getInstance().reloadMessages();
                    Pinecone.getInstance().reloadMessagesObject();
                    sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Gray + "Reloading data objects...");
                    Pinecone.getInstance().getCheckManager().violations.clear();
                    Pinecone.getInstance().reloadPlayerData();
                    Pinecone.getInstance().getCheckManager().getChecks().clear();
                    Pinecone.getInstance().getCheckManager().initializeDetections();
                    Pinecone.getInstance().loadChecks();
                    break;
                }
                case "config": {
                    sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Gray + "Reloading configurations...");
                    Pinecone.getInstance().reloadConfig();
                    Pinecone.getInstance().reloadConfigObject();
                    Pinecone.getInstance().reloadPlayerData();
                    Pinecone.getInstance().reloadMessages();
                    Pinecone.getInstance().reloadMessagesObject();
                    break;
                }
                case "data": {
                    sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Gray + "Reloading data objects...");
                    Pinecone.getInstance().reloadPlayerData();
                    Pinecone.getInstance().getCheckManager().violations.clear();
                    break;
                }
                default: {
                    sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.translate("&c&oIncorrect arguments \"" + args[1] + "\". Defaulting to &f&ofull&c&o."));
                    sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Gray + "Reloading configurations...");
                    Pinecone.getInstance().reloadConfig();
                    Pinecone.getInstance().reloadConfigObject();
                    Pinecone.getInstance().reloadMessages();
                    Pinecone.getInstance().reloadMessagesObject();
                    sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Gray + "Reloading data objects...");
                    Pinecone.getInstance().reloadPlayerData();
                    Pinecone.getInstance().getCheckManager().getChecks().clear();
                    Pinecone.getInstance().loadChecks();
                    Pinecone.getInstance().getCheckManager().violations.clear();
                }
            }
            sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Green + "Done!");
        } else {
            sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.translate("&cInvalid Arguments! &7Options: &ffull&7, &fconfig&7, &fdata&7."));
        }
    }
}
