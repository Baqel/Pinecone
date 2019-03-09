package xyz.baqel.pinecone.events.custom;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import xyz.baqel.pinecone.events.system.Cancellable;
import xyz.baqel.pinecone.events.system.Event;

@Getter
@Setter
public class PacketAttackEvent extends Event implements Cancellable {

    private boolean cancelled;
    private Player attacker;
    private LivingEntity attacked;

    public PacketAttackEvent(Player attacker, LivingEntity attacked) {
        this.attacker = attacker;
        this.attacked = attacked;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
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
