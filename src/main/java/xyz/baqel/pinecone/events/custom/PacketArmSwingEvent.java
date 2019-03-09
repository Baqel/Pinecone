package xyz.baqel.pinecone.events.custom;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import xyz.baqel.pinecone.events.system.Event;

@Getter
public class PacketArmSwingEvent extends Event {

    private Player player;
    private boolean lookingAtBlock;

    public PacketArmSwingEvent(Player player, boolean lookingAtBlock) {
        this.player = player;
        this.lookingAtBlock = lookingAtBlock;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
