package ryoske.api.profile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.mineskin.SkinOptions;
import org.mineskin.Variant;
import org.mineskin.Visibility;
import ryoske.Ryoske;
import ryoske.api.intefaces.Updatable;
import ryoske.api.player.RyoskeNPC;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

public class RyoskeProfile extends GameProfile implements Updatable {

    private boolean shouldUpdate;

    public RyoskeProfile(RyoskeNPC npc) {
        super(npc.uuid(), npc.name());
    }

    private void clearCurrentTexture() {
        for (Property textures : getProperties().get("textures")) {
            super.getProperties().remove("textures", textures);
        }
    }

    public RyoskeProfile clearSkin() {
        this.clearCurrentTexture();
        this.shouldUpdate = true;
        return this;
    }

    public RyoskeProfile skin(Variant variant, String url) {
        this.clearCurrentTexture();
        Ryoske.getMineskinClient().generateUrl(url, SkinOptions.create("skin", variant, Visibility.PUBLIC)).thenAccept(skin -> {
            super.getProperties().put("textures", new Property("textures", skin.data.texture.value, skin.data.texture.signature));
            this.shouldUpdate = true;
        });
        return this;
    }

    public RyoskeProfile skin(Variant variant, File file) {
        this.clearCurrentTexture();
        try (FileInputStream stream = new FileInputStream(file)) {
            Ryoske.getMineskinClient().generateUpload(stream, SkinOptions.create("skin", variant, Visibility.PUBLIC)).thenAccept(skin -> {
                super.getProperties().put("textures", new Property("textures", skin.data.texture.value, skin.data.texture.signature));
                this.shouldUpdate = true;
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return this;
    }

    public RyoskeProfile skin(Variant variant, UUID uuid) {
        this.clearCurrentTexture();
        Ryoske.getMineskinClient().generateUser(uuid, SkinOptions.create("skin", variant, Visibility.PUBLIC)).thenAccept(skin -> {
            super.getProperties().put("textures", new Property("textures", skin.data.texture.value, skin.data.texture.signature));
            this.shouldUpdate = true;
        });
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
