package xyz.baqel.pinecone.commands.pinecone.args;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.commands.BaqelArgument;
import xyz.baqel.pinecone.utils.Color;

public class MenuArgument extends BaqelArgument {
    public MenuArgument() {
        super("menu", "menu", "Opens the Pinecone GUI", "pinecone.menu");

        addAlias("gui");
    }

    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Pinecone.getInstance().getGuiManager().openInventory((Player) sender, "Pinecone-Menu");
        } else {
            sender.sendMessage(Pinecone.getInstance().getMessageFields().prefix + Color.Red + "Only players can use this command.");
        }
    }
}
