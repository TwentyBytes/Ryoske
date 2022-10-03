package ryoske.api.settings;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import ryoske.api.player.RyoskeNPC;

public class Settings {

    private final RyoskeSkinSettings skinSettings;
    private final RyoskeHandSettings handSettings;
    private final RyoskeNameSettings nameSettings;
    private final RyoskePoseSettings poseSettings;
    private final RyoskeSettings settings;

    public Settings(RyoskeNPC npc) {
        this.skinSettings = new RyoskeSkinSettings(npc, 0);
        this.handSettings = new RyoskeHandSettings();
        this.nameSettings = new RyoskeNameSettings(LegacyComponentSerializer.legacySection().deserialize(npc.name()));
        this.poseSettings = new RyoskePoseSettings();
        this.settings = new RyoskeSettings();
    }

    public RyoskeSkinSettings skinSettings() {
        return skinSettings;
    }

    public RyoskeHandSettings handSettings() {
        return handSettings;
    }

    public RyoskeNameSettings nameSettings() {
        return nameSettings;
    }

    public RyoskePoseSettings poseSettings() {
        return poseSettings;
    }

    public RyoskeSettings settings() {
        return settings;
    }

}
