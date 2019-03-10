package xyz.baqel.pinecone.detections.combat.reach;

import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.CheckType;
import xyz.baqel.pinecone.detections.combat.reach.detections.TypeA;

public class Reach extends Check {
    public Reach() {
        super("Reach", CheckType.COMBAT, true, false, false, false, 20 ,7);

        addDetection(new TypeA(this, "Type A", true, true));
    }
}
