package xyz.baqel.pinecone.detections.combat.autoclicker;

import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.CheckType;
import xyz.baqel.pinecone.detections.combat.autoclicker.detections.TypeA;
import xyz.baqel.pinecone.detections.combat.autoclicker.detections.TypeB;
import xyz.baqel.pinecone.detections.combat.autoclicker.detections.TypeC;

import java.util.concurrent.TimeUnit;

public class AutoClicker extends Check {

    public AutoClicker() {
        super("AutoClicker", CheckType.COMBAT, true, true, false, false, 50, TimeUnit.MINUTES.toMillis(10), 20);

        addDetection(new TypeA(this, "Type A", true, true));
        addDetection(new TypeB(this, "Type B", true, true));
        addDetection(new TypeC(this, "Type C", true, true));
    }
}
