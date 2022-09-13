package ryoske.api.visibility;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ryoske.api.player.RyoskeNPC;
import ryoske.api.util.VisibilityUtil;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Visibility {

    protected final Set<String> seers = new HashSet<>();
    protected RyoskeNPC npc;
    private VisibilityType visibilityType;

    public Visibility(RyoskeNPC npc, VisibilityType visibilityType) {
        this.npc = npc;
        this.visibilityType = visibilityType;

        visibilityType.setVisibility(this);
    }

    public void tick() {
        Set<String> clear = visibilityType.clearSeers();
        Set<String> add = visibilityType.newSeers();

        seers.removeAll(clear);
        seers.addAll(add);

        for (String seer : add) {
            Player player = Bukkit.getPlayerExact(seer);
            if (player == null) {
                continue;
            }
            VisibilityUtil.show(npc, player);
            npc.showSettings(player);
        }
        for (String seer : clear) {
            Player player = Bukkit.getPlayerExact(seer);
            if (player == null) {
                continue;
            }
            VisibilityUtil.hide(npc, player);
        }
    }

    public Visibility add(Player... players) {
        for (Player player : players) {
            seers.add(player.getName());
        }
        return this;
    }

    public Visibility remove(Player... players) {
        for (Player player : players) {
            seers.remove(player.getName());
        }
        return this;
    }

    public Visibility visibilityType(VisibilityType visibilityType) {
        if (this.visibilityType != null) {
            this.visibilityType.clear();
            for (String seer : this.seers) {
                Player player = Bukkit.getPlayerExact(seer);
                if (player == null) {
                    continue;
                }
                VisibilityUtil.hide(npc, player);
            }
            this.seers.clear();
        }

        this.visibilityType = visibilityType;
        this.visibilityType.setVisibility(this);
        this.visibilityType.start();
        return this;
    }

    public Set<String> seers() {
        return seers;
    }

    public Set<Player> seersAsPlayers() {
        Set<Player> players = new HashSet<>();
        for (String seer : seers) {
            Player player = Bukkit.getPlayerExact(seer);
            if (player == null) {
                continue;
            }
            players.add(player);
        }
        return players;
    }

    public boolean seer(Player player) {
        return seer(player.getName());
    }

    public boolean seer(String name) {
        return seers.contains(name);
    }

    public void destroy() {
        for (Player player : seersAsPlayers()) {
            VisibilityUtil.hide(npc, player);
        }
        seers.clear();
    }

}
