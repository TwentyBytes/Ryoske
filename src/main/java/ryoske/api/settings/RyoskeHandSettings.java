package ryoske.api.settings;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import ryoske.api.intefaces.Updatable;

public class RyoskeHandSettings implements Updatable {

    private MainHand hand = MainHand.RIGHT;

    public MainHand hand() {
        return this.hand;
    }

    public RyoskeHandSettings hand(MainHand hand) {
        this.hand = hand;
        this.shouldUpdate = true;
        return this;
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
