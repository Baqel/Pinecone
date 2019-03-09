package xyz.baqel.pinecone.commands;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.utils.Color;
import xyz.baqel.pinecone.utils.JsonMessage;
import xyz.baqel.pinecone.utils.MathUtils;
import xyz.baqel.pinecone.utils.MiscUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class BaqelCommand implements CommandExecutor, TabCompleter {
    private static BaqelCommand instance;
    private final String name;
    private final String display;
    private final String permission;
    private final String description;
    private final List<BaqelArgument> arguments;

    protected BaqelCommand(String name, String display, String description, String permission) {
        this.name = name;
        this.display = display;
        this.description = description;
        this.permission = permission;
        this.arguments = new ArrayList<>();
        instance = this;
        Pinecone.getInstance().getCommand(name).setExecutor(this);
        Pinecone.getInstance().getCommand(name).setTabCompleter(this);
        this.addArguments();
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> toReturn = new ArrayList<>();

        if (label.equalsIgnoreCase(name)) {
            final BaqelArgument[] baqelArgument = new BaqelArgument[1];
            arguments.forEach(argument -> {
                if (args.length == 1 && argument.getName().toLowerCase().startsWith(args[0].toLowerCase()) && !args[0].contains(argument.getName())) {
                    toReturn.add(argument.getName());
                }

                if (argument.getName().equalsIgnoreCase(args[0])) {
                    baqelArgument[0] = argument;
                } else if (getArgumentByAlias(args[0]) != null) {
                    baqelArgument[0] = getArgumentByAlias(args[0]);
                }
            });

            if (baqelArgument[0] != null) {
                baqelArgument[0].getTabComplete().getOrDefault(args.length, Lists.newArrayList()).forEach(string -> {
                    String[] split = string.split(",").length == 0 ? new String[]{string} : string.split(","), conditional = split.length > 1 ? new String[]{split[1], split[2]} : new String[0];

                    String arg = split[0];

                    if (conditional.length > 0) {
                        if (args[Integer.parseInt(conditional[1]) - 1].equalsIgnoreCase(conditional[0].replaceAll("!", "")) == !conditional[0].startsWith("!")) {
                            switch (arg.toLowerCase()) {
                                case "%check%":
                                    Pinecone.getInstance().getCheckManager().getChecks().stream().filter(check -> check.getName().toLowerCase().replaceAll(" ", "_").startsWith(args[args.length - 1].toLowerCase())).forEach(check -> toReturn.add(check.getName().toLowerCase().replaceAll(" ", "_")));
                                    break;
                                case "%detection%":
                                    Check check = Pinecone.getInstance().getCheckManager().getCheckByName(args[args.length - 2]);
                                    assert check != null;
                                    check.getDetections().stream().filter(detection -> detection.getId().toLowerCase().replaceAll(" ", "_").startsWith(args[args.length - 1])).forEach(detection -> toReturn.add(detection.getId().toLowerCase().replaceAll(" ", "_")));
                                    break;
                            }
                        }
                    } else {
                        switch (arg.toLowerCase()) {
                            case "%check%":
                                Pinecone.getInstance().getCheckManager().getChecks().stream().filter(check -> check.getName().toLowerCase().replaceAll(" ", "_").startsWith(args[args.length - 1].toLowerCase())).forEach(check -> toReturn.add(check.getName().toLowerCase().replaceAll(" ", "_")));
                                break;
                            case "%detection%":
                                Check check = Pinecone.getInstance().getCheckManager().getCheckByName(args[args.length - 2]);
                                assert check != null;
                                check.getDetections().stream().filter(detection -> detection.getId().toLowerCase().replaceAll(" ", "_").startsWith(args[args.length - 1])).forEach(detection -> toReturn.add(detection.getId().toLowerCase().replaceAll(" ", "_")));
                                break;
                            default:
                                if (arg.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                                    toReturn.add(arg);
                                }
                                break;
                        }
                    }
                });
            }
        }
        return toReturn.size() == 0 ? null : toReturn;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (this.permission != null && !sender.hasPermission(this.permission)) {
            sender.sendMessage(getName().equals("pinecone") ? Color.Red + "This server is using Pinecone v" + Pinecone.getInstance().getDescription().getVersion() + " by Baqel." : Pinecone.getInstance().getMessageFields().noPermission);
            return true;
        }
        try {
            int page = args.length > 0 ? Integer.parseInt(args[0]) : 1;
            sender.sendMessage(MiscUtils.line(Color.Dark_Gray));
            sender.sendMessage(Color.Pink + this.display + Color.Green + " Command Help " + Color.Gray + "Page (" + page + " / " + (int) MathUtils.round(arguments.size() / 6D) + ")");
            sender.sendMessage("");
            sender.sendMessage(Color.translate("&b<> &7= required. &b[] &7= optional."));
            sender.sendMessage("");
            if (sender instanceof Player) {
                for (int i = (page - 1) * 6; i < Math.min(page * 6, arguments.size()); i++) {
                    BaqelArgument argument = arguments.get(i);
                    JsonMessage message = new JsonMessage();

                    StringBuilder aliasesFormatted = new StringBuilder();
                    List<String> aliases = argument.getAliases();
                    if (aliases.size() > 0) {
                        for (String aliase : aliases) {
                            aliasesFormatted.append(Color.White).append(aliase).append(Color.Gray).append(", ");
                        }
                        int length = aliasesFormatted.length();
                        aliasesFormatted = new StringBuilder(aliasesFormatted.substring(0, length - 2));
                    } else {
                        aliasesFormatted = new StringBuilder(Color.Red + "None");
                    }

                    String hoverText = Color.translate((argument.getPermission().length > 1 ? "&dPermissions&7: &f" + Arrays.toString(argument.getPermission()) : "&dPermission&7: &f" + argument.getPermission()[0]) + "\n&dAliases&7: " + aliasesFormatted);
                    message.addText(Color.Gray + "/" + Color.Pink + label.toLowerCase() + Color.White + " " + argument.getDisplay() + Color.Gray + " to " + argument.getDescription()).addHoverText(hoverText);
                    message.sendToPlayer((Player) sender);
                }
            } else {
                for (int i = (page - 1) * 6; i < Math.min(arguments.size(), page * 6); i++) {
                    BaqelArgument argument = arguments.get(i);
                    sender.sendMessage(Color.Gray + "/" + label.toLowerCase() + Color.White + " " + argument.getDisplay() + Color.Gray + " to " + argument.getDescription());
                }
            }
            sender.sendMessage(MiscUtils.line(Color.Dark_Gray));
        } catch (Exception e) {
            for (BaqelArgument argument : this.arguments) {
                if (!args[0].equalsIgnoreCase(argument.getName()) && !argument.getAliases().contains(args[0].toLowerCase()))
                    continue;

                if ((argument.getPermission() == null || sender.hasPermission("pinecone.admin")
                        || sender.hasPermission(permission))) {
                    argument.onArgument(sender, cmd, args);
                    break;
                }
            }
        }
        return true;
    }

    private BaqelArgument getArgumentByAlias(String alias) {
        return arguments.stream().filter(arg -> arg.getAliases().stream().anyMatch(alias2 -> alias2.equalsIgnoreCase(alias))).findFirst().orElse(null);
    }

    protected abstract void addArguments();
}
