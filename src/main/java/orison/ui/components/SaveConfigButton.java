package orison.ui.components;

import static orison.core.OrisonMod.makeUIPath;

import com.badlogic.gdx.graphics.Texture;
import orison.core.configs.JsonConfig;
import orison.utils.TexLoader;

public class SaveConfigButton extends ConfigButton {

    public static final Texture ICON = TexLoader.getTexture(makeUIPath("save.png"));

    public SaveConfigButton(float cX, float cY, int slotIndex) {
        super(cX, cY, ICON);
        onClick = () -> {
            JsonConfig.saveCustomPreset(slotIndex);
            return null;
        };
    }
}
