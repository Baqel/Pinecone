package xyz.baqel.pinecone.detections.world.scaffold;

import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.CheckType;
import xyz.baqel.pinecone.detections.world.scaffold.detections.TypeA;

public class Scaffold extends Check {
    public Scaffold() {
        super("Scaffold", CheckType.WORLD, true, true, false, false, 25, 0);

        addDetection(new TypeA(this, "Type A", true, false));
    }
}
