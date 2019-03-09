package com.ngxdev.tinyprotocol.packet.in;

import com.ngxdev.tinyprotocol.api.NMSObject;
import com.ngxdev.tinyprotocol.api.ProtocolVersion;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.baqel.pinecone.Pinecone;
import xyz.baqel.pinecone.data.PlayersData;

@Getter
public class WrappedInArmAnimationPacket extends NMSObject {
    private static final String packet = Client.ARM_ANIMATION;
    private boolean punchingBlock;

    public WrappedInArmAnimationPacket(Object object, Player player) {
        super(object, player);
    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        PlayersData data = Pinecone.getInstance().getDataManager().getPlayerData(player);

        punchingBlock = data != null && (data.breakingBlock || player.getItemInHand().getType().equals(Material.FISHING_ROD));
    }
}
