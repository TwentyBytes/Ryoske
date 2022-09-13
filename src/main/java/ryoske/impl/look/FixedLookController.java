package ryoske.impl.look;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ryoske.api.look.LookController;
import ryoske.api.player.RyoskeNPC;

public class FixedLookController implements LookController {

    private final RyoskeNPC npc;
    private Location eyes;

    private float yaw;
    private float pitch;

    public FixedLookController(RyoskeNPC npc, Location eyes, float yaw, float pitch) {
        this.npc = npc;
        this.eyes = eyes;
        this.eyes.setYaw(yaw);
        this.eyes.setPitch(pitch);
    }

    public FixedLookController yaw(float yaw) {
        this.yaw = yaw;

        this.update();
        return this;
    }

    public FixedLookController pitch(float pitch) {
        this.yaw = pitch;

        this.update();
        return this;
    }

    public FixedLookController look(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;

        this.update();
        return this;
    }

    public void update() {
        this.eyes = npc.locationController().location();
        this.eyes.setYaw(yaw);
        this.eyes.setPitch(pitch);
    }

    @Override
    public RyoskeNPC owner() {
        return npc;
    }

    @Override
    public Location eyes(Player player) {
        return eyes;
    }

}
