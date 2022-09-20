package ryoske.api.player;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
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

import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class RyoskeNPC {

    public static final TIntObjectMap<RyoskeNPC> NPC_BY_ID = new TIntObjectHashMap<>();

    protected int id;
    private BukkitTask update;

    public RyoskeNPC() {
        this.id = Entity.nextEntityId();
        NPC_BY_ID.put(id, this);
    }

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

    public RyoskeNPC destroy() {
        NPC_BY_ID.remove(id);
        if (this.update != null) {
            this.update.cancel();
            this.update = null;
            visibility().destroy();
        }
        return this;
    }

    public RyoskeNPC spawn(long spawnDelay) {
        destroy();
        NPC_BY_ID.put(id, this);

        long delay = tickDelay();
        this.update = Bukkit.getScheduler().runTaskTimer(Ryoske.getPlugin(), this::dataTick, spawnDelay, delay);
        return this;
    }

    public RyoskeNPC spawn() {
        return spawn(100);
    }

    public int id() {
        return id;
    }

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

    public void tick() {
    }

}
