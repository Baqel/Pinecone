package xyz.baqel.pinecone;

import com.ngxdev.tinyprotocol.api.ProtocolVersion;
import com.ngxdev.tinyprotocol.api.TinyProtocolHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.baqel.pinecone.commands.BaqelCommandManager;
import xyz.baqel.pinecone.data.DataManager;
import xyz.baqel.pinecone.data.ReliabilitySystem;
import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.CheckManager;
import xyz.baqel.pinecone.detections.Detection;
import xyz.baqel.pinecone.events.bukkit.LatestListeners;
import xyz.baqel.pinecone.events.bukkit.DataEvents;
import xyz.baqel.pinecone.events.bukkit.PlayerConnectListeners;
import xyz.baqel.pinecone.events.custom.PineconePunishEvent;
import xyz.baqel.pinecone.events.system.EventManager;
import xyz.baqel.pinecone.profiling.ToggleableProfiler;
import xyz.baqel.pinecone.utils.*;
import xyz.baqel.pinecone.utils.blockbox.BlockBoxManager;
import xyz.baqel.pinecone.utils.gui.GUIManager;


import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;

@Getter
public class Pinecone extends JavaPlugin {
    private static Pinecone instance;
    public Set<Player> bannedPlayers;
    public double tps = -1;
    public ConsoleCommandSender consoleSender;
    public ToggleableProfiler profile = new ToggleableProfiler();
    public ToggleableProfiler specificProfile = new ToggleableProfiler();
    public ScheduledExecutorService executorService;
    public ExecutorService executorOne;
    public ExecutorService executorTwo;
    public ExecutorService executorThree;
    public String serverVersion;
    public CommandSender debuggingPlayer;
    private TinyProtocolHandler protocol;
    private CheckManager checkManager;
    private Detection detections;
    private BaqelCommandManager commandManager;
    private Messages messageFields;
    private File messagesFile;
    private FileConfiguration messages;
    private GUIManager guiManager;
    private ReliabilitySystem system;
    private BlockBoxManager blockBoxManager;
    private long timeCheckStarted;
    private DataManager dataManager;
    private int currentTick;
    private String Prefix;

    public static Pinecone getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        consoleSender = Bukkit.getConsoleSender();

        consoleSender.sendMessage(Color.translate("&cInitializing Pinecone..."));
        serverVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        instance = this;
        MiscUtils.printToConsole("&cLoading configuration objects...");

        startupConfiguration();

        MiscUtils.printToConsole("&cFiring up the thread turbines...");

        initializeThreads();

        MiscUtils.printToConsole(MiscUtils.line(Color.Dark_Gray));
        MiscUtils.printToConsole("&2Allocating ramen seasoning...");
        MiscUtils.printToConsole("&2Boiling the water...");
        MiscUtils.printToConsole("&2Cooking noodles...");
        MiscUtils.printToConsole("&2Putting noodles in bowl...");
        MiscUtils.printToConsole("&2Introducing seasoning packet bowl of noodles...");
        MiscUtils.printToConsole("&2Stirring the pot...");
        MiscUtils.printToConsole("&2Successfully ate ramen! We got 'em chief!");
        MiscUtils.printToConsole(MiscUtils.line(Color.Dark_Gray));

        MiscUtils.printToConsole("");
        MiscUtils.printToConsole("&aRegistered objects:");
//        new BukkitRunnable() {
//            public void run() {
//                protocol = new TinyProtocolHandler();
//                blockBoxManager = new BlockBoxManager();
////                getLogger().log(Level.INFO, "Starting download process...");
//            }
//        }.runTaskLater(this, 20L);
        protocol = new TinyProtocolHandler();
        blockBoxManager = new BlockBoxManager();
        MiscUtils.printToConsole("&7- Packet Handler");
        dataManager = new DataManager();
        MiscUtils.printToConsole("&7- Data Handler");
        checkManager = new CheckManager();
        MiscUtils.printToConsole("&7- Check Handler");
        new BlockUtils();
        new ReflectionsUtil();
        new Color();
        new MiscUtils();

        MiscUtils.printToConsole("&7- Utilities");
        messageFields = new Messages();
        commandManager = new BaqelCommandManager();
        bannedPlayers = new HashSet<>();
        guiManager = new GUIManager();
        system = new ReliabilitySystem();
        MiscUtils.printToConsole("&7- Misc Data");

        MiscUtils.printToConsole("&cLoading data objects...");
        loadUsers();

        MiscUtils.printToConsole("&cFinalizing...");
        loadChecks();
        registerEvents();
        runTasks();

        MiscUtils.printToConsole("&aCompleted!");
    }

    private String unloadPlugin(String p1) {
        PluginManager pm = getServer().getPluginManager();
        SimplePluginManager spm = (SimplePluginManager)pm;
        SimpleCommandMap cmdMap = null;
        List plugins = null;
        Map names = null;
        Map commands = null;
        Map listeners = null;
        boolean reloadlisteners = true;
        if (spm != null) {
            try {
                Field tp = spm.getClass().getDeclaredField("plugins");
                tp.setAccessible(true);
                plugins = (List)tp.get(spm);
                Field arr$ = spm.getClass().getDeclaredField("lookupNames");
                arr$.setAccessible(true);
                names = (Map)arr$.get(spm);

                Field len$;
                try {
                    len$ = spm.getClass().getDeclaredField("listeners");
                    len$.setAccessible(true);
                    listeners = (Map)len$.get(spm);
                } catch (Exception var19) {
                    reloadlisteners = false;
                }

                len$ = spm.getClass().getDeclaredField("commandMap");
                len$.setAccessible(true);
                cmdMap = (SimpleCommandMap)len$.get(spm);
                Field i$ = cmdMap.getClass().getDeclaredField("knownCommands");
                i$.setAccessible(true);
                commands = (Map)i$.get(cmdMap);
            } catch (IllegalAccessException | NoSuchFieldException var20) {
                return "Failed to unload plugin!";
            }
        }

        String var21 = "";
        Plugin[] var22 = getServer().getPluginManager().getPlugins();
        int var23 = var22.length;

        for (int var24 = 0; var24 < var23; ++var24) {
            Plugin p = var22[var24];
            if (p.getDescription().getName().equalsIgnoreCase(p1)) {
                pm.disablePlugin(p);
                var21 = var21 + p.getName() + " ";
                if (plugins != null && plugins.contains(p)) {
                    plugins.remove(p);
                }

                if (names != null && names.containsKey(p1)) {
                    names.remove(p1);
                }

                Iterator it;
                if (listeners != null && reloadlisteners) {
                    it = listeners.values().iterator();

                    while (it.hasNext()) {
                        SortedSet entry = (SortedSet)it.next();
                        Iterator c = entry.iterator();

                        while (c.hasNext()) {
                            RegisteredListener value = (RegisteredListener)c.next();
                            if (value.getPlugin() == p) {
                                c.remove();
                            }
                        }
                    }
                }

                if (cmdMap != null) {
                    it = commands.entrySet().iterator();

                    while (it.hasNext()) {
                        Map.Entry var25 = (Map.Entry) it.next();
                        if (var25.getValue() instanceof PluginCommand) {
                            PluginCommand var26 = (PluginCommand)var25.getValue();
                            if (var26.getPlugin() == p) {
                                var26.unregister(cmdMap);
                                it.remove();
                            }
                        }
                    }
                }
            }
        }

        return var21 + "has been unloaded and disabled!";
    }

    /* Cleaning up Pinecone */
    @Override
    public void onDisable() {
        clearPineconeInstances();
        clearBukkitInstances();
    }

    private void initializeThreads() {
        int available = Runtime.getRuntime().availableProcessors();

        if (available >= 4) {
            executorOne = Executors.newSingleThreadExecutor();
            executorTwo = Executors.newSingleThreadExecutor();
            executorThree = Executors.newFixedThreadPool(3);
            executorService = Executors.newSingleThreadScheduledExecutor();
        } else {
            executorOne = Executors.newSingleThreadExecutor();
            executorTwo = executorOne;
            executorThree = executorOne;
            executorService = Executors.newSingleThreadScheduledExecutor();
        }
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerConnectListeners(), this);
        Bukkit.getPluginManager().registerEvents(new DataEvents(), this);

        if (ProtocolVersion.getGameVersion().isOrAbove(ProtocolVersion.V1_13)) {
            Bukkit.getPluginManager().registerEvents(new LatestListeners(), this);
        }
        EventManager.register(new DataEvents());
    }

    private void startupConfiguration() {
        saveDefaultConfig();
        new Config();
        createMessages();
    }

    private void runTasks() {
        /* TPS Check */
        new BukkitRunnable() {
            long sec;
            long currentSec;
            long ticks;

            public void run() {
                sec = (System.currentTimeMillis() / 1000L);
                if (currentSec == sec) {
                    ticks += 1;
                } else {
                    currentSec = sec;
                    tps = (tps == 0.0D ? ticks : (tps + ticks) / 2.0D);
                    ticks = 1;
                }
                currentTick++;
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    private void loadUsers() {
        reloadPlayerData();
    }

    public void executeOnPlayer(Player player, Check check, Detection detection) {
        PineconePunishEvent event = new PineconePunishEvent(player, check);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            new BukkitRunnable() {
                public void run() {
                    for (String string : getConfig().getStringList("executableCommands")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getMessageFields().translateWithPrefix(string
                            .replaceAll("%player%", player.getName())
                            .replaceAll("%check%", check.getName())
                            .replaceAll("%nickname%", check.getNickName() + " (" + detection.getNickname() + ")")));
                    }
                }
            }.runTask(this);
            if (!check.isTestMode()) bannedPlayers.add(player);
        }
    }

    public void clearPineconeInstances() {
        consoleSender.sendMessage(Color.translate("&cPinecone's Custom Instances Cleared:"));
        getCheckManager().unregisterAll();
        consoleSender.sendMessage(Color.translate("&7- Checks"));
        EventManager.clearRegistered();
        consoleSender.sendMessage(Color.translate("&7- Events"));
        getDataManager().getDataObjects().clear();
        executorService.shutdown();
        consoleSender.sendMessage(Color.translate("&7- Objects"));
        getCommandManager().removeAllCommands();
        getCheckManager().violations.clear();
        consoleSender.sendMessage(Color.translate("&7- Commands"));
    }

    public void clearBukkitInstances() {
        consoleSender.sendMessage(Color.translate("&cPinecone's Bukkit Instances Cleared:"));
        Pinecone.getInstance().getServer().getScheduler().cancelTasks(this);
        consoleSender.sendMessage(Color.translate("&7- Tasks"));
        HandlerList.unregisterAll(Pinecone.getInstance());
        consoleSender.sendMessage(Color.translate("&7- Listeners"));
        getCommandManager().removeAllCommands();
        consoleSender.sendMessage(Color.translate("&7- Commands"));
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this);
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this);
        consoleSender.sendMessage(Color.translate("&7- PME"));
    }

    public void loadChecks() {
        for (Check check : getCheckManager().getChecks()) {
            String pathToCheck = "checks." + check.getName() + ".";
            if (getConfig().get("checks." + check.getName()) == null) {
                getConfig().set(pathToCheck + "enabled", check.isEnabled());
                getConfig().set(pathToCheck + "executable", check.isExecutable());
                getConfig().set(pathToCheck + "cancellable", check.isCancellable());
                getConfig().set(pathToCheck + "cancelThreshold", check.getCancelThreshold());
                getConfig().set(pathToCheck + "executableThreshold", check.getExecutableThreshold());
                getConfig().set(pathToCheck + "nickname", check.getNickName() == null ? check.getName() : check.getNickName());
                for (String name : check.getConfigValues().keySet()) {
                    getConfig().set(pathToCheck + "values." + name, check.getConfigValues().get(name));
                }
                saveConfig();
            }
            check.setEnabled(getConfig().getBoolean(pathToCheck + "enabled"));
            check.setExecutable(getConfig().getBoolean(pathToCheck + "executable"));
            check.setCancellable(getConfig().getBoolean(pathToCheck + "cancellable"));
            check.setCancelThreshold(getConfig().getInt(pathToCheck + "cancelThreshold"));
            check.setExecutableThreshold(getConfig().getInt(pathToCheck + "executableThreshold"));
            check.setNickName(getConfig().getString(pathToCheck + "nickname"));
            for (String name : check.getConfigValues().keySet()) {
                if (getConfig().get("checks." + check.getName() + ".values." + name) != null) {
                    check.getConfigValues().put(name, getConfig().get("checks." + check.getName() + ".values." + name));
                } else {
                    getConfig().set("checks." + check.getName() + ".values." + name, check.getConfigValues().get(name));
                    saveConfig();
                }
            }
            for (Detection detection : check.getDetections()) {
                String pathToDetection = pathToCheck + "detections." + detection.getId() + ".";
                if (getConfig().get(pathToCheck + "detections." + detection.getId()) == null) {
                    getConfig().set(pathToDetection + "enabled", detection.isEnabled());
                    getConfig().set(pathToDetection + "executable", detection.isExecutable());
                    getConfig().set(pathToDetection + "cancellable", detection.isCancellable());
                    getConfig().set(pathToDetection + "nickname", detection.getNickname() == null ? detection.getId() : detection.getNickname());
                    for (String name : detection.getConfigValues().keySet()) {
                        getConfig().set(pathToDetection + "values." + name, detection.getConfigValues().get(name));
                    }
                    saveConfig();
                }
                detection.setEnabled(getConfig().getBoolean(pathToDetection + "enabled"));
                detection.setExecutable(getConfig().getBoolean(pathToDetection + "executable"));
                detection.setNickname(getConfig().getString(pathToDetection + "nickname"));
                for (String name : detection.getConfigValues().keySet()) {
                    if (getConfig().get(pathToDetection + "values." + name) != null) {
                        detection.getConfigValues().put(name, getConfig().get(pathToDetection + "values." + name));
                    } else {
                        getConfig().set(pathToDetection + "values."+ name, detection.getConfigValues().get(name));
                        saveConfig();
                    }
                }
            }
        }
    }
    public void saveChecks() {
        for (Check check : getCheckManager().getChecks()) {
            String pathToCheck =  "checks." + check.getName() + ".";
            getConfig().set(pathToCheck + "enabled", check.isEnabled());
            getConfig().set(pathToCheck + "executable", check.isExecutable());
            getConfig().set(pathToCheck + "cancellable", check.isCancellable());
            getConfig().set(pathToCheck + "cancelThreshold", check.getCancelThreshold());
            getConfig().set(pathToCheck + "executableThreshold", check.getExecutableThreshold());
            getConfig().set(pathToCheck + "nickname", check.getNickName() == null ? check.getName() : check.getNickName());

            for (String name : check.getConfigValues().keySet()) {
                getConfig().set(pathToCheck + "values." + name, check.getConfigValues().get(name));
            }

            for (Detection detection : check.getDetections()) {
                String pathToDetection = pathToCheck + "detections." + detection.getId() + ".";
                if (getConfig().get(pathToCheck + "detections." + detection.getId()) == null) {
                    getConfig().set(pathToDetection + "enabled", detection.isEnabled());
                    getConfig().set(pathToDetection + "executable", detection.isExecutable());
                    getConfig().set(pathToDetection + "cancellable", detection.isCancellable());
                    getConfig().set(pathToDetection + "nickname", detection.getNickname() == null ? detection.getId() : detection.getNickname());
                    for (String name : detection.getConfigValues().keySet()) {
                        getConfig().set(pathToDetection + "values." + name, detection.getConfigValues().get(name));
                    }
                }
            }
        }
        saveConfig();
    }
    public FileConfiguration getMessages() {
        if (messages == null) {
            reloadMessages();
        }
        return messages;
    }

    public void saveMessages() {
        if (messages == null || messagesFile == null) {
            return;
        }
        try {
            getMessages().save(messagesFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + messagesFile, ex);
        }
    }

    public void createMessages() {
        if (messagesFile == null) {
            messagesFile = new File(getDataFolder(), "messages.yml");
        }
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
    }

    public void reloadMessages() {
        if (messagesFile == null) {
            messagesFile = new File(getDataFolder(), "messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        /* Look for defaults in the jar */
        try {
            Reader defConfigStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            messages.setDefaults(defConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadConfigObject() {
        new Config();
    }

    public void reloadMessagesObject() {
        this.messageFields = new Messages();
    }

    public void reloadPlayerData() {
        getDataManager().getDataObjects().clear();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            getDataManager().createDataObject(player);
        }
    }
}
