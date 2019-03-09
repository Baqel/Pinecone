package xyz.baqel.pinecone.events.custom;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import xyz.baqel.pinecone.events.system.Cancellable;
import xyz.baqel.pinecone.events.system.Event;

@Getter
public class PacketSendEvent extends Event implements Cancellable {
    private Player player;
    @Setter
    private Object packet;
    private boolean cancelled;
    private String type;

    public PacketSendEvent(Player player, Object packet, String type) {
        this.player = player;
        this.packet = packet;
        this.type = type;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
