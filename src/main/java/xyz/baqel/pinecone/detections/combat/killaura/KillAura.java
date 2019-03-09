package xyz.baqel.pinecone.detections.combat.killaura;

import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.CheckType;

import java.util.concurrent.TimeUnit;

public class KillAura extends Check {
    public KillAura() {
        super("KillAura", CheckType.COMBAT, true, true, false, false, 50, TimeUnit.MINUTES.toMillis(4), 5);


    }
}
