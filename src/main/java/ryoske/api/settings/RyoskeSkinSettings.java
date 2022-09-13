package ryoske.api.settings;

import lombok.Getter;
import org.mineskin.Variant;
import ryoske.api.intefaces.Updatable;
import ryoske.api.player.RyoskeNPC;

import java.io.File;
import java.util.UUID;

public class RyoskeSkinSettings implements Updatable {

    private final RyoskeNPC npc;
    @Getter
    private int raw;

    public RyoskeSkinSettings(RyoskeNPC owner, int raw) {
        this.npc = owner;
        this.raw = raw;
    }

    public boolean cape() {
        return (raw & 0x01) != 0;
    }

    public RyoskeSkinSettings cape(boolean enabled) {
        if (enabled) {
            raw |= 0x01;
        } else {
            raw &= ~0x01;
        }

        shouldUpdate = true;
        return this;
    }

    public boolean jacket() {
        return (raw & 0x02) != 0;
    }

    public RyoskeSkinSettings jacket(boolean enabled) {
        if (enabled) {
            raw |= 0x02;
        } else {
            raw &= ~0x02;
        }

        shouldUpdate = true;
        return this;
    }

    public boolean leftSleeve() {
        return (raw & 0x04) != 0;
    }

    public RyoskeSkinSettings leftSleeve(boolean enabled) {
        if (enabled) {
            raw |= 0x04;
        } else {
            raw &= ~0x04;
        }

        shouldUpdate = true;
        return this;
    }

    public boolean rightSleeve() {
        return (raw & 0x08) != 0;
    }

    public RyoskeSkinSettings rightSleeve(boolean enabled) {
        if (enabled) {
            raw |= 0x08;
        } else {
            raw &= ~0x08;
        }

        shouldUpdate = true;
        return this;
    }

    public boolean leftPants() {
        return (raw & 0x10) != 0;
    }

    public RyoskeSkinSettings leftPants(boolean enabled) {
        if (enabled) {
            raw |= 0x10;
        } else {
            raw &= ~0x10;
        }

        shouldUpdate = true;
        return this;
    }

    public boolean rightPants() {
        return (raw & 0x20) != 0;
    }

    public RyoskeSkinSettings rightPants(boolean enabled) {
        if (enabled) {
            raw |= 0x20;
        } else {
            raw &= ~0x20;
        }

        this.shouldUpdate = true;
        return this;
    }

    public boolean hats() {
        return (raw & 0x40) != 0;
    }

    public RyoskeSkinSettings hats(boolean enabled) {
        if (enabled) {
            raw |= 0x40;
        } else {
            raw &= ~0x40;
        }

        this.shouldUpdate = true;
        return this;
    }

    public RyoskeSkinSettings enableAll() {
        raw |= 0x01;
        raw |= 0x02;
        raw |= 0x04;
        raw |= 0x08;
        raw |= 0x10;
        raw |= 0x20;
        raw |= 0x40;
        this.shouldUpdate = true;
        return this;
    }

    public RyoskeSkinSettings disableAll() {
        raw &= ~0x01;
        raw &= ~0x02;
        raw &= ~0x04;
        raw &= ~0x08;
        raw &= ~0x10;
        raw &= ~0x20;
        raw &= ~0x40;
        this.shouldUpdate = true;
        return this;
    }

    public RyoskeSkinSettings skin(Variant variant, String url) {
        npc.profile().skin(variant, url);
        return this;
    }

    public RyoskeSkinSettings skin(Variant variant, File file) {
        npc.profile().skin(variant, file);
        return this;
    }

    public RyoskeSkinSettings skin(Variant variant, UUID uuid) {
        npc.profile().skin(variant, uuid);
        return this;
    }

    @Override
    public String toString() {
        return "RyoskeSkinSettings{" +
                "raw=" + raw +
                ", capeEnabled=" + cape() +
                ", jacketEnabled=" + jacket() +
                ", leftSleeveEnabled=" + leftSleeve() +
                ", rightSleeveEnabled=" + rightSleeve() +
                ", leftPantsEnabled=" + leftPants() +
                ", rightPantsEnabled=" + rightPants() +
                ", hatsEnabled=" + hats() +
                '}';
    }

    private boolean shouldUpdate;

    @Override
    public boolean shouldUpdate() {
        return shouldUpdate;
    }

    @Override
    public void updated() {
        this.shouldUpdate = false;
    }

}
