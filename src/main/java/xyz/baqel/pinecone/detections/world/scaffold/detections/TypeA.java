package xyz.baqel.pinecone.detections.world.scaffold.detections;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.baqel.pinecone.data.PlayersData;
import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.Detection;

public class TypeA extends Detection {
    public TypeA(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        setExperimental(true);
    }

    @Override
    public void onBukkitEvent(Event event, PlayersData data) {
        if (event instanceof BlockPlaceEvent) {
            BlockPlaceEvent e = (BlockPlaceEvent) event;

            if (data.generalCancel
                    || e.getPlayer().getLocation().getBlockY() <= e.getBlockPlaced().getY()
                    || e.getPlayer().isSneaking()
                    || data.movement.deltaXZ < 0.2
                    || data.airTicks > 0) {
                return;
            }

            if (e.getPlayer().getLocation().clone().subtract(0, 1, 0).getBlock().getType().isSolid()
                    && !e.getPlayer().getLocation().clone().subtract(0, 2, 0).getBlock().getType().isSolid()) {
                if (data.scaffoldTopVerbose.flag(4, 1000L)) {
                    flag(data, "t: air", 1, true, true);
                }
            }
            debug(data, data.scaffoldTopVerbose.getVerbose() + ": " + (e.getPlayer().getLocation().clone().subtract(0, 1, 0).getBlock().getType().isSolid()
                    && !e.getPlayer().getLocation().clone().subtract(0, 2, 0).getBlock().getType().isSolid()));
        }
    }
}
