package ryoske;

import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineskin.MineskinClient;
import ryoske.api.interact.InteractHandle;
import ryoske.api.interact.Interaction;
import ryoske.api.player.RyoskeNPC;

public class Ryoske extends JavaPlugin {

    @Getter
    private static Plugin plugin;

    @Getter
    private static String mineSkinKey;

    @Getter @Setter
    private static MineskinClient mineskinClient;

    @SneakyThrows
    public static void init(Plugin plugin) {
        init(plugin, null);
    }

    public static void init(Plugin plugin, String mineSkinKey) {
        Ryoske.plugin = plugin;
        Ryoske.mineSkinKey = mineSkinKey;
        Ryoske.mineskinClient = new MineskinClient("Ryoske", mineSkinKey);

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new Listener() {

            @EventHandler
            private void handle(PlayerUseUnknownEntityEvent event) {
                RyoskeNPC npc = RyoskeNPC.get(event.getEntityId());
                if (npc == null) {
                    return;
                }

                Interaction interaction = event.isAttack() ? Interaction.ATTACK : Interaction.INTERACT;

                for (InteractHandle interact : npc.interacts()) {
                    if (interact.type() != interaction) {
                        continue;
                    }
                    interact.interact(npc, event.getPlayer());
                }
            }

            @EventHandler
            private void handle(PluginDisableEvent event) {
                if (event.getPlugin().getName().equalsIgnoreCase(plugin.getName())) {
                    for (RyoskeNPC value : RyoskeNPC.NPC_BY_ID.values(new RyoskeNPC[0])) {
                        value.destroy();
                    }
                }
            }

        }, plugin);
    }

    @Override
    public void onEnable() {
        init(this);
    }

}
