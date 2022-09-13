package ryoske.api.look;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ryoske.api.player.RyoskeNPC;

public interface LookController {

    RyoskeNPC owner();

    Location eyes(Player nearby);

    void update();

}
