package ryoske.impl.location;

import org.bukkit.Location;
import ryoske.api.location.LocationController;
import ryoske.api.look.LookController;
import ryoske.api.player.RyoskeNPC;
import ryoske.impl.look.FixedLookController;
import ryoske.impl.look.NearbyLookController;

public class DefaultLocationController implements LocationController {

    private final RyoskeNPC npc;
    private LookController lookController;

    private Location location;
    private Location sleep;

    private boolean shouldUpdate;

    public DefaultLocationController(RyoskeNPC npc, Location location) {
        this.npc = npc;
        this.location = location;
        this.sleep = location;
        this.lookController = new FixedLookController(npc, location.clone().add(0, 2, 0), 0, 0);
    }

    public DefaultLocationController lookController(LookController controller) {
        this.lookController = controller;
        return this;
    }

    @Override
    public RyoskeNPC owner() {
        return npc;
    }

    @Override
    public Location location() {
        return location.clone();
    }

    @Override
    public Location locationOriginal() {
        return location;
    }

    @Override
    public Location sleep() {
        return sleep.clone();
    }

    @Override
    public Location sleepOriginal() {
        return sleep;
    }

    public RyoskeNPC sleep(Location location) {
        this.sleep = location;
        shouldUpdate = true;
        return npc;
    }

    @Override
    public LookController lookController() {
        return lookController;
    }

    @Override
    public void setLookController(LookController controller) {
        this.lookController = controller;
    }

    @Override
    public void teleport(Location location) {
        this.location = location;
        this.sleep = location;
        this.lookController.update();

        shouldUpdate = true;
    }

    @Override
    public boolean shouldUpdate() {
        return lookController instanceof NearbyLookController || shouldUpdate;
    }

    @Override
    public void updated() {
        shouldUpdate = false;
    }

}
