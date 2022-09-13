package ryoske.api.interact;

import org.bukkit.entity.Player;
import ryoske.api.player.RyoskeNPC;

public interface InteractHandle {

    Interaction type();

    void interact(RyoskeNPC npc, Player player);

}
