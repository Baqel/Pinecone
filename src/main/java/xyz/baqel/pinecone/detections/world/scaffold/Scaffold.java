package xyz.baqel.pinecone.detections.world.scaffold;

import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.CheckType;
import xyz.baqel.pinecone.detections.world.scaffold.detections.TypeA;
import xyz.baqel.pinecone.detections.world.scaffold.detections.TypeB;
import xyz.baqel.pinecone.detections.world.scaffold.detections.TypeC;

public class Scaffold extends Check {
    public Scaffold() {
        super("Scaffold", CheckType.WORLD, true, true, false, false, 25, 0);

        addDetection(new TypeB(this, "Type B", true, true));
        addDetection(new TypeA(this, "Type A", true, false));
        addDetection(new TypeC(this, "Type C", false, false));
    }
}
