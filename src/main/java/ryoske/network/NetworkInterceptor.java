package ryoske.network;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.protocol.game.*;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import ryoske.api.interact.InteractHandle;
import ryoske.api.interact.Interaction;
import ryoske.api.player.RyoskeNPC;

public class NetworkInterceptor implements Listener {

    public boolean read(Player player, @NotNull ChannelHandlerContext ctx, @NotNull Object some) {
        if (some instanceof ServerboundInteractPacket packet) {
            RyoskeNPC npc = RyoskeNPC.get(packet.getEntityId());
            if (npc == null) {
                return true;
            }

            Interaction interaction;
            switch (packet.getActionType()) {
                case ATTACK -> interaction = Interaction.ATTACK;
                case INTERACT -> interaction = Interaction.INTERACT;
                case INTERACT_AT -> interaction = Interaction.INTERACT_AT;
                default -> throw new IllegalStateException("Not found interact type with name -> " + packet.getActionType().name());
            }

            for (InteractHandle interact : npc.interacts()) {
                if (interact.type() == interaction) {
                    interact.interact(npc, player);
                }
            }
        }
        return true;
    }

    public boolean write(Player player, ChannelHandlerContext ctx, Object packet, ChannelPromise promise) {
        return true;
    }

    @EventHandler
    private void interceptPlayer(PlayerJoinEvent event) {
        intercept(event.getPlayer());
    }

    public void intercept(Player player) {
        CraftPlayer craft = (CraftPlayer) player;
        ChannelPipeline pipeline = craft.getHandle().networkManager.channel.pipeline();

        if (pipeline.get(player.getName()) != null) {
            pipeline.remove(player.getName());
        }

        ChannelDuplexHandler handler = new ChannelDuplexHandler() {

            @Override
            public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object some) throws Exception {
                if (NetworkInterceptor.this.read(player, ctx, some)) {
                    super.channelRead(ctx, some);
                }
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object some, ChannelPromise promise) throws Exception {
                if (NetworkInterceptor.this.write(player, ctx, some, promise)) {
                    super.write(ctx, some, promise);
                }
            }
        };

        pipeline.addBefore("packet_handler", player.getName(), handler);
    }

}
