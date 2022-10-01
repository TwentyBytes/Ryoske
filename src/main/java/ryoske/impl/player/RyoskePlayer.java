package ryoske.impl.player;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import ryoske.api.equipment.RyoskeEquipment;
import ryoske.api.interact.InteractHandle;
import ryoske.api.location.LocationController;
import ryoske.api.player.RyoskeNPC;
import ryoske.api.profile.RyoskeProfile;
import ryoske.api.settings.Settings;
import ryoske.api.visibility.Visibility;
import ryoske.impl.location.DefaultLocationController;
import ryoske.impl.visibility.VisibilityNearby;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RyoskePlayer extends RyoskeNPC {

    private final UUID uuid;
    private final RyoskeProfile profile;
    private final List<InteractHandle> interacts = new ArrayList<>();
    private final RyoskeEquipment equipment = new RyoskeEquipment();
    private final Visibility visibility;
    private final Settings settings;

    private LocationController locationController;
    private String name;
    private double viewDistance = 25.0D;

    public RyoskePlayer(Plugin plugin, UUID uuid, String name, Location location) {
        super(plugin);
        this.uuid = uuid;
        this.name = name;
        this.profile = new RyoskeProfile(this);
        this.locationController = new DefaultLocationController(this, location);
        this.visibility = new Visibility(this, new VisibilityNearby());
        this.settings = new Settings(this);
    }

    public RyoskePlayer locationController(LocationController controller) {
        this.locationController = controller;
        return this;
    }

    public RyoskePlayer viewDistance(double value) {
        this.viewDistance = value;
        return this;
    }

    public RyoskePlayer name(String value) {
        this.name = value;
        return this;
    }

    public RyoskePlayer interacts(InteractHandle... handles) {
        this.interacts.addAll(Arrays.asList(handles));
        return this;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public UUID uuid() {
        return uuid;
    }

    @Override
    public LocationController locationController() {
        return locationController;
    }

    @Override
    public List<InteractHandle> interacts() {
        return interacts;
    }

    @Override
    public double viewDistance() {
        return viewDistance;
    }

    @Override
    public long tickDelay() {
        return 1;
    }

    @Override
    public RyoskeProfile profile() {
        return profile;
    }

    @Override
    public RyoskeEquipment equipment() {
        return equipment;
    }

    @Override
    public Settings settings() {
        return settings;
    }

    @Override
    public Visibility visibility() {
        return visibility;
    }

}
