package ryoske.api.settings;

import net.kyori.adventure.text.Component;
import ryoske.api.intefaces.Updatable;

public class RyoskeNameSettings implements Updatable {

    private Component prefix = Component.empty();
    private Component suffix = Component.empty();
    private Component display;
    private boolean shouldUpdate;

    public RyoskeNameSettings(Component display) {
        this.display = display;
    }

    public Component prefix() {
        return prefix;
    }

    public RyoskeNameSettings prefix(Component text) {
        this.prefix = text;
        this.shouldUpdate = true;

        return this;
    }

    public RyoskeNameSettings display(Component text) {
        this.display = text;
        this.shouldUpdate = true;

        return this;
    }

    public Component display() {
        return display;
    }

    public RyoskeNameSettings suffix(Component text) {
        this.suffix = text;
        this.shouldUpdate = true;

        return this;
    }

    public Component suffix() {
        return suffix;
    }

    @Override
    public boolean shouldUpdate() {
        return shouldUpdate;
    }

    @Override
    public void updated() {
        this.shouldUpdate = false;
    }

}
