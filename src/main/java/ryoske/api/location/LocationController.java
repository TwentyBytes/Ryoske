package ryoske.api.location;

import org.bukkit.Location;
import ryoske.api.intefaces.Updatable;
import ryoske.api.look.LookController;
import ryoske.api.player.RyoskeNPC;

public interface LocationController extends Updatable {

    RyoskeNPC owner();

    Location location();

    Location locationOriginal();

    Location sleep();

    Location sleepOriginal();

    LookController lookController();

    void setLookController(LookController controller);

    void teleport(Location location);

}
