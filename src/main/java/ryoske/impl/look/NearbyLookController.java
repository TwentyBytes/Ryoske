package ryoske.impl.look;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ryoske.api.look.LookController;
import ryoske.api.player.RyoskeNPC;
import ryoske.api.util.LookUtil;

public class NearbyLookController implements LookController {

    private final RyoskeNPC npc;

    public NearbyLookController(RyoskeNPC npc) {
        this.npc = npc;
    }

    @Override
    public RyoskeNPC owner() {
        return npc;
    }

    @Override
    public Location eyes(Player nearby) {
        return LookUtil.lookAt(npc, nearby.getEyeLocation());
    }

    @Override
    public void update() {
    }

}
