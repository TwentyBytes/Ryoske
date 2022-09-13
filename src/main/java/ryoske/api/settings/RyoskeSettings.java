package ryoske.api.settings;

import ryoske.api.intefaces.Updatable;

public class RyoskeSettings implements Updatable {

    private boolean collision = false;
    private boolean nameTagVisibility = false;
    private boolean shouldUpdate;

    public boolean collision() {
        return this.collision;
    }

    public RyoskeSettings collision(boolean state) {
        this.collision = state;
        this.shouldUpdate = true;
        return this;
    }

    public boolean nameTagVisibility() {
        return this.nameTagVisibility;
    }

    public RyoskeSettings nameTagVisibility(boolean state) {
        this.nameTagVisibility = state;
        this.shouldUpdate = true;
        return this;
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
