package ryoske.api.util;

import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MainHand;
import ryoske.api.animaton.Animation;
import ryoske.api.player.RyoskeNPC;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class VisibilityUtil {

    static Field field;

    public static void show(RyoskeNPC npc, Player... players) {
        FriendlyByteBuf byteBuf;

        byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeEnum(ClientboundPlayerInfoPacket.Action.ADD_PLAYER);
        byteBuf.writeVarInt(1);
        byteBuf.writeGameProfile(npc.profile());
        byteBuf.writeVarInt(GameType.CREATIVE.getId());
        byteBuf.writeVarInt(0);
        byteBuf.writeNullable(LegacyComponentSerializer.legacySection().deserialize(npc.name()), FriendlyByteBuf::writeComponent);
        byteBuf.writeNullable(null, FriendlyByteBuf::writePublicKey);

        ClientboundPlayerInfoPacket packet = new ClientboundPlayerInfoPacket(byteBuf);
        PacketUtil.sendPacket(packet, players);

        for (Player player : players) {
            Location eyes = npc.locationController().lookController()
                    .eyes(player);
            Location original = npc.locationController().locationOriginal();

            byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeVarInt(npc.id());
            byteBuf.writeUUID(npc.uuid());
            byteBuf.writeDouble(original.getX());
            byteBuf.writeDouble(original.getY());
            byteBuf.writeDouble(original.getZ());
            byteBuf.writeByte((int) (eyes.getYaw() * 256.0F / 360.0F));
            byteBuf.writeByte((int) (eyes.getPitch() * 256.0F / 360.0F));
            ClientboundAddPlayerPacket playerPacket = new ClientboundAddPlayerPacket(byteBuf);

            PacketUtil.sendPacket(playerPacket, players);
        }
    }

    public static void hide(RyoskeNPC npc, Player... players) {
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeEnum(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER);
        byteBuf.writeVarInt(1);
        byteBuf.writeUUID(npc.uuid());

        ClientboundPlayerInfoPacket packet = new ClientboundPlayerInfoPacket(byteBuf);
        PacketUtil.sendPacket(packet, players);

        ClientboundRemoveEntitiesPacket remove = new ClientboundRemoveEntitiesPacket(npc.id());
        PacketUtil.sendPacket(remove, players);
    }

    public static void refreshDisplayName(RyoskeNPC npc, Player... players) {
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeEnum(ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME);
        byteBuf.writeVarInt(1);
        byteBuf.writeUUID(npc.uuid());
        byteBuf.writeNullable(LegacyComponentSerializer.legacySection().deserialize(npc.name()), FriendlyByteBuf::writeComponent);

        ClientboundPlayerInfoPacket packet = new ClientboundPlayerInfoPacket(byteBuf);
        PacketUtil.sendPacket(packet, players);
    }

    public static void refreshSkin(RyoskeNPC npc, Player... players) {
        hide(npc, players);
        show(npc, players);
    }

    public static void refreshPosition(RyoskeNPC npc, Player... players) {
        for (Player player : players) {
            Location location = npc.locationController().locationOriginal();
            Location eyes = npc.locationController().lookController().eyes(player);

            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeVarInt(npc.id());
            byteBuf.writeDouble(location.getX());
            byteBuf.writeDouble(location.getY());
            byteBuf.writeDouble(location.getZ());
            byteBuf.writeByte((int) (eyes.getYaw() * 256.0F / 360.0F));
            byteBuf.writeByte((int) (eyes.getPitch() * 256.0F / 360.0F));
            byteBuf.writeBoolean(true);

            ClientboundTeleportEntityPacket pos = new ClientboundTeleportEntityPacket(byteBuf);

            byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeVarInt(npc.id());
            byteBuf.writeByte((int) (eyes.getYaw() * 256.0F / 360.0F));
            ClientboundRotateHeadPacket head = new ClientboundRotateHeadPacket(byteBuf);

            PacketUtil.sendPacket(pos, player);
            PacketUtil.sendPacket(head, player);
        }
    }

    public static void refreshEquipment(RyoskeNPC npc, Player... players) {
        List<Pair<EquipmentSlot, ItemStack>> equipment = new ArrayList<>();
        for (EquipmentSlot value : EquipmentSlot.values()) {
            org.bukkit.inventory.ItemStack stack = npc.equipment().get(value);
            if (stack.getType() == Material.AIR) {
                continue;
            }
            equipment.add(Pair.of(value, CraftItemStack.asNMSCopy(stack)));
        }

        if (equipment.isEmpty()) {
            return;
        }

        ClientboundSetEquipmentPacket packet = new ClientboundSetEquipmentPacket(npc.id(), equipment);
        PacketUtil.sendPacket(packet, players);
    }

    public static void refreshSettings(RyoskeNPC npc, Player... players) {
        //ClientboundSetPlayerTeamPacket

        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeComponent(LegacyComponentSerializer.legacySection().deserialize(npc.name()));
        byteBuf.writeByte(0);
        byteBuf.writeUtf(npc.settings().settings().nameTagVisibility() ? "always" : "never");
        byteBuf.writeUtf(npc.settings().settings().collision() ? "always" : "never");
        byteBuf.writeEnum(ChatFormatting.RESET);
        byteBuf.writeComponent(npc.settings().nameSettings().prefix());
        byteBuf.writeComponent(npc.settings().nameSettings().suffix());
        ClientboundSetPlayerTeamPacket.Parameters parameters = new ClientboundSetPlayerTeamPacket.Parameters(byteBuf);

        byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeUtf(npc.name());
        byteBuf.writeByte(0);
        parameters.write(byteBuf);
        byteBuf.writeVarInt(1);
        byteBuf.writeUtf(npc.name());

        ClientboundSetPlayerTeamPacket packet = new ClientboundSetPlayerTeamPacket(byteBuf);
        PacketUtil.sendPacket(packet, players);
    }

    @SneakyThrows
    public static void refreshEntityData(RyoskeNPC npc, Player... players) {
        if (field == null) {
            field = net.minecraft.world.entity.player.Player.class.getDeclaredField("bP");
            field.setAccessible(true);
        }

        List<SynchedEntityData.DataItem<?>> items = new ArrayList<>();
        items.add(new SynchedEntityData.DataItem<>(ServerPlayer.DATA_PLAYER_MODE_CUSTOMISATION, (byte) npc.settings().skinSettings().getRaw()));
        items.add(new SynchedEntityData.DataItem<>((EntityDataAccessor<Byte>) field.get(null), (byte) (npc.settings().handSettings().hand() == MainHand.RIGHT ? 1 : 0)));

        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());

        byteBuf.writeVarInt(npc.id());
        SynchedEntityData.pack(items, byteBuf);

        ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(byteBuf);
        PacketUtil.sendPacket(packet, players);
    }

    public static void playAnimation(RyoskeNPC npc, Animation animation, Player... players) {
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeVarInt(npc.id());
        byteBuf.writeByte(animation.getAction());
        ClientboundAnimatePacket packet = new ClientboundAnimatePacket(byteBuf);

        PacketUtil.sendPacket(packet, players);
    }

}
