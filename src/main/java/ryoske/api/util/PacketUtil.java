package ryoske.api.util;

import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketUtil {

    public static void sendPacket(Packet<?> packet, Player... players) {
        for (Player player : players) {
            ((CraftPlayer) player).getHandle().connection.send(packet);
        }
    }

}
