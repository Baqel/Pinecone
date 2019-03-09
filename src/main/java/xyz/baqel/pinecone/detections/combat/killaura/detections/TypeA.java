package xyz.baqel.pinecone.detections.combat.killaura.detections;

import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.Detection;

public class TypeA extends Detection {
    public TypeA(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        addConfigValue("threshold.normal", 14);
        addConfigValue("", 25);
        addConfigValue("", 1);
        addConfigValue("", 1);
    }
}
