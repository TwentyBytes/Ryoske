package ryoske;

import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineskin.MineskinClient;
import org.mineskin.Variant;
import ryoske.api.animaton.Animation;
import ryoske.api.interact.InteractHandle;
import ryoske.api.interact.Interaction;
import ryoske.api.player.RyoskeNPC;
import ryoske.impl.look.NearbyLookController;
import ryoske.impl.player.RyoskePlayer;
import ryoske.network.NetworkInterceptor;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Ryoske extends JavaPlugin {

    @Getter
    private static Plugin plugin;

    @Getter
    private static String mineSkinKey;

    @Getter
    private static MineskinClient mineskinClient;

    @Override
    public void onEnable() {
        init(this);
        getDataFolder().mkdir();
    }

    @SneakyThrows
    public static void init(Plugin plugin) {
        init(plugin, null);

        RyoskePlayer npc = new RyoskePlayer(ThreadLocalRandom.current().nextInt(100000), UUID.randomUUID(), "Hello", new Location(Bukkit.getWorld("world"), 0.5, 102, 0.5));
        npc.locationController().setLookController(new NearbyLookController(npc));

        npc.interacts(new InteractHandle() {
            @Override
            public Interaction type() {
                return Interaction.ATTACK;
            }

            @Override
            public void interact(RyoskeNPC npc, Player player) {
                player.sendMessage("Педик?");
                npc.playAnimation(Animation.HURT);
                npc.playAnimation(Animation.CRITICAL_HIT);
                npc.playAnimation(Animation.MAGIC_CRITICAL_HIT);
                npc.playAnimation(Animation.WAKE_UP);
            }
        });

        npc.settings().skinSettings().enableAll();
        npc.profile().skin(Variant.CLASSIC, "https://s.namemc.com/i/ba819a3a90d2a043.png");
        npc.settings().nameSettings().prefix(Component.text("Пидорас?"));
        npc.settings().settings().nameTagVisibility(true);
        npc.equipment().set(EquipmentSlot.MAINHAND, new ItemStack(Material.DIAMOND_SWORD));
        npc.equipment().set(EquipmentSlot.OFFHAND, new ItemStack(Material.DIAMOND_SWORD));
        npc.viewDistance(10);
    }

    public static void init(Plugin plugin, String mineSkinKey) {
        Ryoske.plugin = plugin;
        Ryoske.mineSkinKey = mineSkinKey;

        mineskinClient = new MineskinClient("Ryoske", mineSkinKey);

        PluginManager manager = Bukkit.getPluginManager();
        NetworkInterceptor interceptor = new NetworkInterceptor();
        manager.registerEvents(interceptor, plugin);
        manager.registerEvents(new Listener() {

            @EventHandler
            private void handle(PluginDisableEvent event) {
                if (event.getPlugin().getName().equalsIgnoreCase(plugin.getName())) {
                    for (RyoskeNPC value : RyoskeNPC.NPC_BY_ID.values(new RyoskeNPC[0])) {
                        value.destroy();
                    }
                }
            }

        }, plugin);

        for (Player player : Bukkit.getOnlinePlayers()) {
            interceptor.intercept(player);
        }
    }

}
