package ryoske.api.player;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ryoske.Ryoske;
import ryoske.api.animaton.Animation;
import ryoske.api.equipment.RyoskeEquipment;
import ryoske.api.interact.InteractHandle;
import ryoske.api.location.LocationController;
import ryoske.api.profile.RyoskeProfile;
import ryoske.api.settings.Settings;
import ryoske.api.util.VisibilityUtil;
import ryoske.api.visibility.Visibility;

import java.util.*;

public abstract class RyoskeNPC {

    public static final TIntObjectMap<RyoskeNPC> NPC_BY_ID = new TIntObjectHashMap<>();

    private BukkitTask update;

    public RyoskeNPC(int id) {
        NPC_BY_ID.put(id, this);

        respawn();
    }

    public RyoskeNPC destroy() {
        NPC_BY_ID.remove(id());
        if (this.update != null) {
            this.update.cancel();
            this.update = null;
            visibility().destroy();
        }
        return this;
    }

    public RyoskeNPC respawn() {
        destroy();
        if (NPC_BY_ID.containsKey(id())) {
            throw new IllegalStateException("NPC with id -> " + id() + " already registered...");
        }
        NPC_BY_ID.put(id(), this);

        long delay = tickDelay();
        this.update = Bukkit.getScheduler().runTaskTimer(Ryoske.getPlugin(), this::dataTick, 100, delay);
        return this;
    }

    public abstract int id();

    public abstract String name();

    public abstract UUID uuid();

    public abstract LocationController locationController();

    public abstract List<InteractHandle> interacts();

    public abstract double viewDistance();

    public abstract long tickDelay();

    public abstract RyoskeProfile profile();

    public abstract RyoskeEquipment equipment();

    public abstract Settings settings();

    public abstract Visibility visibility();

    private void dataTick() {
        this.visibility().tick();

        Set<Player> set = visibility().seersAsPlayers();
        Player[] players = set.toArray(new Player[0]);

        if (equipment().shouldUpdate()) {
            VisibilityUtil.refreshEquipment(this, players);
            equipment().updated();
        }

        if (profile().shouldUpdate()) {
            VisibilityUtil.refreshSkin(this, players);
            profile().updated();
        }

        if (locationController().shouldUpdate()) {
            VisibilityUtil.refreshPosition(this, players);
            locationController().updated();
        }

        if (settings().skinSettings().shouldUpdate() || settings().handSettings().shouldUpdate()) {
            VisibilityUtil.refreshEntityData(this, players);
            settings().skinSettings().updated();
            settings().handSettings().updated();
        }

        if (settings().nameSettings().shouldUpdate() || settings().settings().shouldUpdate()) {
            VisibilityUtil.refreshDisplayName(this, players);
            VisibilityUtil.refreshSettings(this, players);
            settings().nameSettings().updated();
            settings().settings().updated();
        }

        this.tick();
    }

    public void showSettings(Player... players) {
        VisibilityUtil.refreshSkin(this, players);
        VisibilityUtil.refreshPosition(this, players);
        VisibilityUtil.refreshEntityData(this, players);
        VisibilityUtil.refreshDisplayName(this, players);
        VisibilityUtil.refreshSettings(this, players);
        VisibilityUtil.refreshEquipment(this, players);
    }

    public RyoskeNPC playAnimation(Animation animation) {
        return playAnimation(animation, visibility().seersAsPlayers().toArray(new Player[0]));
    }

    public RyoskeNPC playAnimation(Animation animation, Player... players) {
        VisibilityUtil.playAnimation(this, animation, players);
        return this;
    }

    public void tick() {}

    @SuppressWarnings("all")
    public static <T extends RyoskeNPC> T get(int id) {
        return (T) NPC_BY_ID.get(id);
    }

    @SuppressWarnings("all")
    public static <T extends RyoskeNPC> T get(Class<T> clazz, int id) {
        return (T) NPC_BY_ID.get(id);
    }

    public static TIntObjectMap<RyoskeNPC> registry() {
        return NPC_BY_ID;
    }

}
