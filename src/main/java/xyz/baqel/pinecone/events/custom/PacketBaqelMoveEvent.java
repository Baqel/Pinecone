package xyz.baqel.pinecone.events.custom;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import xyz.baqel.pinecone.events.system.Cancellable;
import xyz.baqel.pinecone.events.system.Event;
import xyz.baqel.pinecone.utils.PineconeLocation;

@Getter
public class PacketBaqelMoveEvent extends Event implements Cancellable {

    private Player player;
    private PineconeLocation from, to;
    private boolean cancelled, onGround, jumped;

    public PacketBaqelMoveEvent(Player player, PineconeLocation from, PineconeLocation to, boolean onGround, boolean jumped) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.onGround = onGround;
        this.jumped = jumped;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
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
