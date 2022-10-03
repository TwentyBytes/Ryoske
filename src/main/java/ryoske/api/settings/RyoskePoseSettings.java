package ryoske.api.settings;

import ryoske.api.intefaces.Updatable;
import ryoske.api.util.Pose;

public class RyoskePoseSettings implements Updatable {

    private Pose pose = Pose.STANDING;
    private boolean shouldUpdate;

    public RyoskePoseSettings pose(Pose pose) {
        this.shouldUpdate = true;
        this.pose = pose;

        return this;
    }

    public Pose pose() {
        return pose;
    }

    @Override
    public boolean shouldUpdate() {
        return shouldUpdate;
    }

    @Override
    public void updated() {
        shouldUpdate = true;
    }

}
