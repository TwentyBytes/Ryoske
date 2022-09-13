package ryoske.api.util;

import org.bukkit.Location;
import ryoske.api.player.RyoskeNPC;

public class LookUtil {

    public static Location lookAt(RyoskeNPC npc, Location at) {
        Location from = npc.locationController().location();
        from.add(0, 2, 0);

        double dx = at.getX() - from.getX();
        double dy = at.getY() - from.getY();
        double dz = at.getZ() - from.getZ();

        dy /= Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Math.atan2(-dx, dz) / Math.PI * 180);
        float pitch = (float) (Math.atan(-dy) / Math.PI * 180);

        from.setYaw(yaw);
        from.setPitch(pitch);
        return from;
    }

}
