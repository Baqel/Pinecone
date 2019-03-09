package xyz.baqel.pinecone.utils.gui;

import com.google.common.collect.Sets;
import com.ngxdev.tinyprotocol.api.ProtocolVersion;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.Detection;
import xyz.baqel.pinecone.utils.Color;
import xyz.baqel.pinecone.utils.Config;
import xyz.baqel.pinecone.utils.MiscUtils;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GUIManager {
    private Set<GUI> guiSet;

    public GUIManager() {
        guiSet = Sets.newHashSet();

        addMainGUI();
        addCheckMenu();
        addCheckGUIs();
    }

    private void addCheckMenu() {
        GUI gui = new GUI("Pinecone-Check-Menu", "&d&lCheck Settings", 3);

        createItem(gui, new GUI.GUIItem(MiscUtils.createItem(Material.PAPER, 1, "&dToggle"), 11, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Check-Toggle", false)));
        createItem(gui, new GUI.GUIItem(MiscUtils.createItem(Material.PAPER, 1, "&dCancellable"), 13, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Check-Cancellable", false)));
        createItem(gui, new GUI.GUIItem(MiscUtils.createItem(Material.PAPER, 1, "&dExecutable"), 15, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Check-Executable", false)));
        createItem(gui, new GUI.GUIItem(MiscUtils.createItem(Material.REDSTONE, 1, "&cBack"), 26, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Menu")));
        addGUI(gui);
    }

    private void addCheckGUIs() {
        String[] guis = new String[]{"Toggle", "Cancellable", "Executable"};

        for (String name : guis) {
            GUI gui = new GUI("Pinecone-Check-" + name, "&d&lCheck " + (!name.equals("Toggle") ? name : "On/Off"), 6);

            for (int i = 0; i < Pinecone.getInstance().getCheckManager().getChecks().size(); i++) {
                Check check = Pinecone.getInstance().getCheckManager().getChecks().get(i);

                GUI detectionGui = new GUI("Pinecone-Detection-" + check.getName() + "-" + name, "&d&lDetection " + (!name.equals("Toggle") ? name : "On/Off"), 3);

                for (int i2 = 0; i2 < check.getDetections().size(); i2++) {
                    Detection detection = check.getDetections().get(i2);
                    switch (name) {
                        case "Toggle":
                            createItem(detectionGui, new GUI.GUIItem(MiscUtils.createItem((detection.isEnabled() ? Material.ENCHANTED_BOOK : Material.BOOK), 1, Color.Pink + detection.getId(), "&fLeft click &7to toggle.", "", "&7Enabled: " + (detection.isEnabled() ? "&atrue" : "&cfalse"), "&7Executable: " + (detection.isExecutable() ? "&atrue" : "&cfalse"), "&7Cancellable: " + (detection.isCancellable() ? "&atrue" : "&cfalse")), i2, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.CONSOLE_COMMAND, "pinecone " + name.toLowerCase() + " " + check.getName() + " " + detection.getId(), false)));
                            break;
                        case "Cancellable":
                            createItem(detectionGui, new GUI.GUIItem(MiscUtils.createItem((detection.isCancellable() ? Material.ENCHANTED_BOOK : Material.BOOK), 1, Color.Pink + detection.getId(), "&fLeft click &7to toggle.", "", "&7Enabled: " + (detection.isEnabled() ? "&atrue" : "&cfalse"), "&7Executable: " + (detection.isExecutable() ? "&atrue" : "&cfalse"), "&7Cancellable: " + (detection.isCancellable() ? "&atrue" : "&cfalse")), i2, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.CONSOLE_COMMAND, "pinecone " + name.toLowerCase() + " " + check.getName() + " " + detection.getId(), false)));
                            break;
                        default:
                            createItem(detectionGui, new GUI.GUIItem(MiscUtils.createItem((detection.isExecutable() ? Material.ENCHANTED_BOOK : Material.BOOK), 1, Color.Pink + detection.getId(), "&fLeft click &7to toggle.", "", "&7Enabled: " + (detection.isEnabled() ? "&atrue" : "&cfalse"), "&7Executable: " + (detection.isExecutable() ? "&atrue" : "&cfalse"), "&7Cancellable: " + (detection.isCancellable() ? "&atrue" : "&cfalse")), i2, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.CONSOLE_COMMAND, "pinecone " + name.toLowerCase() + " " + check.getName() + " " + detection.getId(), false)));
                            break;
                    }
                }
                createItem(detectionGui, new GUI.GUIItem(MiscUtils.createItem(Material.REDSTONE, 1, "&cBack"), 26, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Check-" + name)));
                addGUI(detectionGui);
                switch (name) {
                    case "Toggle":
                        createItem(gui, new GUI.GUIItem(MiscUtils.createItem((check.isEnabled() ? Material.ENCHANTED_BOOK : Material.BOOK), 1, Color.Pink + check.getName(), "&fLeft click &7to toggle.", "", "&7Enabled: " + (check.isEnabled() ? "&atrue" : "&cfalse"), "&7Executable: " + (check.isExecutable() ? "&atrue" : "&cfalse"), "&7Cancellable: " + (check.isCancellable() ? "&atrue" : "&cfalse")), i, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.CONSOLE_COMMAND, "pinecone " + name.toLowerCase() + " " + check.getName(), false), new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Detection-" + check.getName() + "-" + name, InventoryAction.MOVE_TO_OTHER_INVENTORY, false)));
                        break;
                    case "Cancellable":
                        createItem(gui, new GUI.GUIItem(MiscUtils.createItem((check.isCancellable() ? Material.ENCHANTED_BOOK : Material.BOOK), 1, Color.Pink + check.getName(), "&fLeft click &7to toggle.", "", "&7Enabled: " + (check.isEnabled() ? "&atrue" : "&cfalse"), "&7Executable: " + (check.isExecutable() ? "&atrue" : "&cfalse"), "&7Cancellable: " + (check.isCancellable() ? "&atrue" : "&cfalse")), i, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.CONSOLE_COMMAND, "pinecone " + name.toLowerCase() + " " + check.getName(), false), new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Detection-" + check.getName() + "-" + name, InventoryAction.MOVE_TO_OTHER_INVENTORY, false)));
                        break;
                    default:
                        createItem(gui, new GUI.GUIItem(MiscUtils.createItem((check.isExecutable() ? Material.ENCHANTED_BOOK : Material.BOOK), 1, Color.Pink + check.getName(), "&fLeft click &7to toggle.", "", "&7Enabled: " + (check.isEnabled() ? "&atrue" : "&cfalse"), "&7Executable: " + (check.isExecutable() ? "&atrue" : "&cfalse"), "&7Cancellable: " + (check.isCancellable() ? "&atrue" : "&cfalse")), i, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.CONSOLE_COMMAND, "pinecone " + name.toLowerCase() + " " + check.getName(), false), new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Detection-" + check.getName() + "-" + name, InventoryAction.MOVE_TO_OTHER_INVENTORY, false)));
                        break;
                }
            }
            createItem(gui, new GUI.GUIItem(MiscUtils.createItem(Material.REDSTONE, 1, "&cBack"), 53, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Check-Menu")));

            addGUI(gui);
        }
    }

    public void updateCheckItems() {
        String[] guis = new String[]{"Toggle", "Cancellable", "Executable"};

        for (String name : guis) {
            Optional<GUI> opGUI = getGuiByName("Pinecone-Check-" + name);
            if (opGUI.isPresent()) {
                GUI gui = opGUI.get();
                gui.getGuiItems().clear();
                for (int i = 0; i < Pinecone.getInstance().getCheckManager().getChecks().size(); i++) {
                    Check check = Pinecone.getInstance().getCheckManager().getChecks().get(i);
                    switch (name) {
                        case "Toggle":
                            createItem(gui, new GUI.GUIItem(MiscUtils.createItem((check.isEnabled() ? Material.ENCHANTED_BOOK : Material.BOOK), 1, Color.Pink + check.getName(), "&fLeft click &7to toggle.", "&fShift + Left click &7to modify detections.", "", "&7Enabled: " + (check.isEnabled() ? "&atrue" : "&cfalse"), "&7Executable: " + (check.isExecutable() ? "&atrue" : "&cfalse"), "&7Cancellable: " + (check.isCancellable() ? "&atrue" : "&cfalse")), i, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.CONSOLE_COMMAND, "pinecone " + name.toLowerCase() + " " + check.getName(), false), new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Detection-" + check.getName() + "-" + name, InventoryAction.MOVE_TO_OTHER_INVENTORY, false)));
                            break;
                        case "Cancellable":
                            createItem(gui, new GUI.GUIItem(MiscUtils.createItem((check.isCancellable() ? Material.ENCHANTED_BOOK : Material.BOOK), 1, Color.Pink + check.getName(), "&fLeft click &7to toggle.", "&fShift + Left click &7to modify detections.", "", "&7Enabled: " + (check.isEnabled() ? "&atrue" : "&cfalse"), "&7Executable: " + (check.isExecutable() ? "&atrue" : "&cfalse"), "&7Cancellable: " + (check.isCancellable() ? "&atrue" : "&cfalse")), i, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.CONSOLE_COMMAND, "pinecone " + name.toLowerCase() + " " + check.getName(), false), new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Detection-" + check.getName() + "-" + name, InventoryAction.MOVE_TO_OTHER_INVENTORY, false)));
                            break;
                        default:
                            createItem(gui, new GUI.GUIItem(MiscUtils.createItem((check.isExecutable() ? Material.ENCHANTED_BOOK : Material.BOOK), 1, Color.Pink + check.getName(), "&fLeft click &7to toggle.", "&fShift + Left click &7to modify detections.", "", "&7Enabled: " + (check.isEnabled() ? "&atrue" : "&cfalse"), "&7Executable: " + (check.isExecutable() ? "&atrue" : "&cfalse"), "&7Cancellable: " + (check.isCancellable() ? "&atrue" : "&cfalse")), i, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.CONSOLE_COMMAND, "pinecone " + name.toLowerCase() + " " + check.getName(), false), new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Detection-" + check.getName() + "-" + name, InventoryAction.MOVE_TO_OTHER_INVENTORY, false)));
                            break;
                    }
                }
                createItem(gui, new GUI.GUIItem(MiscUtils.createItem(Material.REDSTONE, 1, "&cBack"), 53, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Check-Menu")));
            }
        }
    }

    public void updateDetectionItems() {
        String[] guis = new String[]{"Toggle", "Cancellable", "Executable"};

        for (Check check : Pinecone.getInstance().getCheckManager().getChecks()) {
            for (String name : guis) {
                Optional<GUI> opGUI = getGuiByName("Pinecone-Detection-" + check.getName() + "-" + name);
                if (opGUI.isPresent()) {
                    GUI gui = opGUI.get();
                    gui.getGuiItems().clear();
                    for (int i = 0; i < check.getDetections().size(); i++) {
                        Detection detection = check.getDetections().get(i);
                        switch (name) {
                            case "Toggle":
                                createItem(gui, new GUI.GUIItem(MiscUtils.createItem((detection.isEnabled() ? Material.ENCHANTED_BOOK : Material.BOOK), 1, Color.Pink + detection.getId(), "&fLeft click &7to toggle.", "", "&7Enabled: " + (detection.isEnabled() ? "&atrue" : "&cfalse"), "&7Executable: " + (detection.isExecutable() ? "&atrue" : "&cfalse"), "&7Cancellable: " + (detection.isCancellable() ? "&atrue" : "&cfalse")), i, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.CONSOLE_COMMAND, "pinecone " + name.toLowerCase() + " " + check.getName() + " " + detection.getId().replaceAll(" ", "_"), false)));
                                break;
                            case "Cancellable":
                                createItem(gui, new GUI.GUIItem(MiscUtils.createItem((detection.isCancellable() ? Material.ENCHANTED_BOOK : Material.BOOK), 1, Color.Pink + detection.getId(), "&fLeft click &7to toggle.", "", "&7Enabled: " + (detection.isEnabled() ? "&atrue" : "&cfalse"), "&7Executable: " + (detection.isExecutable() ? "&atrue" : "&cfalse"), "&7Cancellable: " + (detection.isCancellable() ? "&atrue" : "&cfalse")), i, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.CONSOLE_COMMAND, "pinecone " + name.toLowerCase() + " " + check.getName() + " " + detection.getId().replaceAll(" ", "_"), false)));
                                break;
                            default:
                                createItem(gui, new GUI.GUIItem(MiscUtils.createItem((detection.isExecutable() ? Material.ENCHANTED_BOOK : Material.BOOK), 1, Color.Pink + detection.getId(), "&fLeft click &7to toggle.", "", "&7Enabled: " + (detection.isEnabled() ? "&atrue" : "&cfalse"), "&7Executable: " + (detection.isExecutable() ? "&atrue" : "&cfalse"), "&7Cancellable: " + (detection.isCancellable() ? "&atrue" : "&cfalse")), i, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.CONSOLE_COMMAND, "pinecone " + name.toLowerCase() + " " + check.getName() + " " + detection.getId().replaceAll(" ", "_"), false)));
                                break;
                        }
                    }
                    createItem(gui, new GUI.GUIItem(MiscUtils.createItem(Material.REDSTONE, 1, "&cBack"), 26, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Check-" + name)));
                }
            }
        }
    }

    private void addMainGUI() {
        GUI gui = new GUI("Pinecone-Menu", "&d&lPinecone Menu", 5);

        if (Config.animationType.equals("RAINBOW")) {
            new ItemAnimation(gui, MiscUtils.createItem(ProtocolVersion.getGameVersion().isOrAbove(ProtocolVersion.V1_13) ? Material.getMaterial("LEGACY_STAINED_GLASS_PANE") : Material.getMaterial("STAINED_GLASS_PANE"), 1, ""), ItemAnimation.AnimationType.RAINBOW, 100L, TimeUnit.MILLISECONDS, 0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 26, 35, 44, 43, 42, 41, 40, 39, 38, 37, 36, 27, 18, 9);
        }

        createItem(gui, new GUI.GUIItem(MiscUtils.createItem(Material.ENCHANTED_BOOK, 1, "&dCheck Menu"), 20, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.OPEN_INVENTORY, "Pinecone-Check-Menu")));
        createItem(gui, new GUI.GUIItem(MiscUtils.createItem(Material.ENCHANTED_BOOK, 1, "&dInformation", "", "&7You are using &fPinecone v" + Pinecone.getInstance().getDescription().getVersion(), "&7by &bBaqel&7.", "", "&7There have been &f" + Pinecone.getInstance().bannedPlayers.size() + " players &7detected today."), 22, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.NONE, "N/A", false)));
        createItem(gui, new GUI.GUIItem(MiscUtils.createItem(Material.ENCHANTED_BOOK, 1, "&cReload Pinecone"), 24, new GUI.GUIItem.Action(GUI.GUIItem.ActionType.PLAYER_COMMAND, "pinecone reload full", false)));
        addGUI(gui);
    }

    public void addGUI(GUI gui) {
        guiSet.add(gui);
    }

    public void createItem(GUI gui, GUI.GUIItem item) {
        gui.addItem(item);
    }

    public void createItem(GUI gui, GUI.GUIItem item, int min, int max) {
        for (int i = min; i < max; i++) {
            gui.addItem(item);
        }
    }

    public Optional<GUI> getGuiByName(String name) {
        Optional<GUI> guiOp = guiSet.stream().filter(gui -> gui.getName().equalsIgnoreCase(name)).findFirst();

        return guiOp;
    }

    public void openInventory(Player player, String name) {
        Optional<GUI> guiOp = getGuiByName(name);

        if (guiOp.isPresent()) {
            GUI gui = guiOp.get();

            player.openInventory(gui.getInventory());
        }
    }}
