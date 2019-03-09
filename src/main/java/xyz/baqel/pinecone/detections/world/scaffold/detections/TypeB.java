package xyz.baqel.pinecone.detections.world.scaffold.detections;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.baqel.pinecone.data.PlayersData;
import xyz.baqel.pinecone.detections.Check;
import xyz.baqel.pinecone.detections.Detection;
import xyz.baqel.pinecone.utils.MathUtils;

public class TypeB extends Detection {
    public TypeB(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);
    }

    @Override
    public void onBukkitEvent(org.bukkit.event.Event event, PlayersData data) {
        if (event instanceof BlockPlaceEvent) {
            BlockPlaceEvent e = (BlockPlaceEvent) event;

            if (!data.generalCancel && e.getBlockPlaced().getType().getId() != 60) {
                long timeDelta;

                Player player = e.getPlayer();

                BlockFace face = e.getBlockAgainst().getFace(e.getBlockPlaced());
                if (face == BlockFace.UP && MathUtils.getHorizontalDistance(e.getBlockPlaced().getLocation(), player.getLocation().getBlock().getLocation()) < 1) {
                    timeDelta = MathUtils.elapsed(data.lastVerticalBlockPlace);

                    Block lastBlock = data.lastTowerBlock == null ? e.getBlockPlaced() : data.lastTowerBlock;
                    if (timeDelta < 400
                            && e.getBlockPlaced().getY() - lastBlock.getY() == 1
                            && MathUtils.getHorizontalDistance(lastBlock.getLocation(), e.getBlockPlaced().getLocation()) == 0) {
                        if (data.scaffoldTopVerbose.flag(4, 800L)) {
                            flag(data, "t: tower d: " + timeDelta, 1, false, true);
                        }
                    } else {
                        data.scaffoldTopVerbose.deduct();
                    }
                    data.lastVerticalBlockPlace = System.currentTimeMillis();
                    data.lastTowerBlock = e.getBlockPlaced();
                }
                if (face != BlockFace.UP && face != BlockFace.DOWN && player.getLocation().getY() - e.getBlockPlaced().getLocation().getY() >= 1.0) {
                    timeDelta = MathUtils.elapsed(data.lastHorizontalBlockPlace);
                    if (timeDelta < 400
                            && e.getPlayer().isOnGround()
                            && data.scaffoldSpeedVerbose.flag(6, 500L)) {
                        data.lastHorizontalBlockPlace = System.currentTimeMillis();
                    }
                }
            }
        }
    }
}
