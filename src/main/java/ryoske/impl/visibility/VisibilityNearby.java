package ryoske.impl.visibility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import ryoske.api.visibility.VisibilityType;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class VisibilityNearby extends VisibilityType {

    @Override
    public Set<String> newSeers() {
        Set<String> newSeers = new HashSet<>();
        Location location = visibility.getNpc().locationController().locationOriginal();
        for (Entity entity : location.getWorld().getEntities()) {
            if (entity.getType() != EntityType.PLAYER) {
                continue;
            }
            if (entity.getLocation().distance(location) > visibility.getNpc().viewDistance()) {
                continue;
            }
            if (visibility.seer(entity.getName())) {
                continue;
            }
            newSeers.add(entity.getName());
        }
        return newSeers;
    }

    @Override
    public Set<String> clearSeers() {
        return visibility.seers().stream().filter(str -> {
            Player player = Bukkit.getPlayerExact(str);
            return player == null || player.getLocation().distance(visibility.getNpc().locationController().locationOriginal()) > visibility.getNpc().viewDistance();
        }).collect(Collectors.toSet());
    }

}
