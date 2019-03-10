package xyz.baqel.pinecone.detections.combat.reach.detections;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.data.PlayersData;
import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.Detection;
import xyz.baqel.pinecone.events.custom.PacketAttackEvent;
import xyz.baqel.pinecone.utils.BoundingBox;
import xyz.baqel.pinecone.utils.MathUtils;
import xyz.baqel.pinecone.utils.MiscUtils;

public class TypeA extends Detection {
    public TypeA(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        addConfigValue("staticThreshold", 3.1);
        addConfigValue("dynamicThreshold", 3.5);
        addConfigValue("latencyMultiplier", 0.0035);
        addConfigValue("velocityMultiplier", 1.3);
        addConfigValue("threshold", 8);
        addConfigValue("resetTime", 2500);

        addConfigValue("violationsToFlag", 3);
        setThreshold((int) getConfigValues().get("violationsToFlag"));
    }

    @Override
    public void onBaqelEvent(Object event, PlayersData data) {
        if (event instanceof PacketAttackEvent) {
            PacketAttackEvent e = (PacketAttackEvent) event;

            if (data.generalCancel) {
                return;
            }

            LivingEntity entity = e.getAttacked();
            BoundingBox entityBox = MiscUtils.getEntityBoundingBox(entity);

            if (Math.abs(entityBox.maxY - entityBox.minY) < 1.4f) {
                return;
            }
            float threshold = (float) ((double) getConfigValues().get(entity.getVelocity().length() < 0.08 ? "staticThreshold" : "dynamicThreshold"));

            if (threshold > 3.0f) {
                threshold += data.ping * (double) getConfigValues().get("latencyMultiplier");

                if (e.getAttacked() instanceof Player) {
                    PlayersData dataAttacked = Pinecone.getInstance().getDataManager().getPlayerData((Player) e.getAttacked());

                    if (dataAttacked != null) {
                        threshold += dataAttacked.ping + (double) getConfigValues().get("latencyMultiplier");
                    }
                }
                threshold += Math.abs(entity.getVelocity().length()) * (double) getConfigValues().get("velocityMultiplier");
            }
            float directionY = (float) Math.abs(e.getAttacker().getEyeLocation().getDirection().normalize().getY()) / 1.3f;

            if (threshold < 3) {
                threshold = 3;
            }

            entityBox = entityBox.grow(threshold, threshold, threshold);

            if (!entityBox.intersectsWithBox(data.boundingBox) && MathUtils.round(entity.getEyeLocation().toVector().distance(e.getAttacker().getEyeLocation().toVector())) > threshold
                    && (data.reachAVerbose.flag((int) getConfigValues().get("threshold"), (int) getConfigValues().get("resetTime"), data.lastReachAttack.hasPassed(10) ? 3 : 1))) {
                flag(data, MathUtils.round(entity.getEyeLocation().toVector().distance(e.getAttacker().getEyeLocation().toVector()), 4) + ">-" + threshold, 1, true, true);
            }

            debug(data, data.reachAVerbose.getVerbose() + ", " + directionY + ", (" + entity.getEyeLocation().toVector().distance(e.getAttacker().getEyeLocation().toVector()) + ">-" + threshold + ")" + ", " + data.lastReachAttack.getPassed());
        }
    }
}
