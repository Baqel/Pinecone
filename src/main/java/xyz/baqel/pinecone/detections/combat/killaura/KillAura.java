package xyz.baqel.pinecone.detections.combat.killaura;

import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.CheckType;
import xyz.baqel.pinecone.detections.combat.killaura.detections.TypeA;

import java.util.concurrent.TimeUnit;

public class KillAura extends Check {
    public KillAura() {
        super("KillAura", CheckType.COMBAT, true, true, false, false, 50, TimeUnit.MINUTES.toMillis(10), 5);

        addDetection(new TypeA(this, "Type A", true, true));
    }
}
