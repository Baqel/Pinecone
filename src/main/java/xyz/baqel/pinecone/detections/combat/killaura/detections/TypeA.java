package xyz.baqel.pinecone.detections.combat.killaura.detections;

import com.ngxdev.tinyprotocol.api.Packet;
import com.ngxdev.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import xyz.baqel.pinecone.data.PlayersData;
import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.Detection;
import xyz.baqel.pinecone.events.custom.PacketReceiveEvent;
import xyz.baqel.pinecone.utils.MathUtils;

public class TypeA extends Detection {
    public TypeA(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        addConfigValue("threshold.normal", 14);
        addConfigValue("threshold.isLagging", 25);
        addConfigValue("verboseToAdd", 1);
        addConfigValue("verboseToDeduct", 1);
    }

    @Override
    public void onBaqelEvent(Object event, PlayersData data) {
        if (event instanceof PacketReceiveEvent) {
            PacketReceiveEvent e = (PacketReceiveEvent) event;

            if (e.getType().equals(Packet.Client.USE_ENTITY)) {
                WrappedInUseEntityPacket use = new WrappedInUseEntityPacket(e.getPacket(), e.getPlayer());

                if (use.getAction() != WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) return;

                long elapsed = MathUtils.elapsed(data.lastFlyingA);
                if (elapsed < 40 && !data.hasLag() && !data.lagTick) {
                    if (data.postKillauraVerbose.flag(data.hasLag() ? (int) getConfigValues().get("threshold.isLagging") : (int) getConfigValues().get("threshold.normal"), data.ping / 2 + 350L, (int) getConfigValues().get("verboseToAdd"))) {
                        flag(data, elapsed + "<-40", 1, true, true);
                    }
                } else {
                    data.postKillauraVerbose.deduct((int) getConfigValues().get("verboseToDeduct"));
                }
                debug(data, data.postKillauraVerbose.getVerbose() + ": " + elapsed);
            } else if (e.getType().contains("Flying") || e.getType().contains("Look") || e.getType().contains("Position")) {
                data.lastFlyingA = System.currentTimeMillis();
            }
        }
    }
}
