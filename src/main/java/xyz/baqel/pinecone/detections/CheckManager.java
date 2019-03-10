package xyz.baqel.pinecone.detections;

import xyz.baqel.pinecone.detections.combat.autoclicker.AutoClicker;
import xyz.baqel.pinecone.detections.combat.killaura.KillAura;
import xyz.baqel.pinecone.detections.combat.reach.Reach;
import xyz.baqel.pinecone.detections.world.scaffold.Scaffold;
import xyz.baqel.pinecone.utils.Violation;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CheckManager {
    private final List<Check> checks;
    public Map<UUID, List<Violation>> violations;

    public CheckManager() {
        checks = new CopyOnWriteArrayList<>();
        violations = new ConcurrentHashMap<>();
        initializeDetections();
    }

    public void initializeDetections() {
        addCheck(new KillAura());
        addCheck(new Scaffold());
        addCheck(new AutoClicker());
        addCheck(new Reach());
    }

    public List<Check> getChecks() {
        return checks;
    }

    public Check getCheckByName(String name) {
        for (Check check : checks) {
            if (check.getName().equalsIgnoreCase(name)) {
                return check;
            }
        }
        return null;
    }

    public boolean isCheck(String name) {
        return getChecks().stream().anyMatch(check -> check.getName().equalsIgnoreCase(name));
    }

    public List<Detection> getDetections(Check check) {
        return check.getDetections();
    }

    private void addCheck(Check check) {
        checks.add(check);
    }

    public void removeCheck(Check check) {
        checks.remove(check);
    }

    public void unregisterAll() {
        checks.clear();
    }
}
