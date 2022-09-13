package ryoske.api.equipment;


import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ryoske.api.intefaces.Updatable;

import java.util.HashMap;
import java.util.Map;

public class RyoskeEquipment implements Updatable {

    private final static ItemStack air = new ItemStack(Material.AIR);

    private final Map<EquipmentSlot, ItemStack> equipment = new HashMap<>();
    private boolean shouldUpdate;

    public ItemStack get(EquipmentSlot slot) {
        return equipment.getOrDefault(slot, air);
    }

    public RyoskeEquipment set(EquipmentSlot slot, ItemStack stack) {
        equipment.put(slot, stack);
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
