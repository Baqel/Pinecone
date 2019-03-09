package xyz.baqel.pinecone.events.custom;

import com.ngxdev.tinyprotocol.packet.in.WrappedInWindowClickPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import xyz.baqel.pinecone.events.system.Event;

public class PacketInvClickEvent extends Event {

    private Player player;
    private WrappedInWindowClickPacket.ClickType action;
    private ItemStack item;

    public PacketInvClickEvent(Player player, WrappedInWindowClickPacket.ClickType action, ItemStack item) {
        this.player = player;
        this.action = action;
        this.item = item;
    }

    public Player getPlayer() {
        return player;
    }

    public WrappedInWindowClickPacket.ClickType getAction() {
        return action;
    }

    public ItemStack getItem() {
    return item;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
