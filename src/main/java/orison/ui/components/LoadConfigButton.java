package orison.ui.components;

import static orison.core.OrisonMod.makeUIPath;

import com.badlogic.gdx.graphics.Texture;
import orison.core.configs.JsonConfig;
import orison.utils.TexLoader;

public class LoadConfigButton extends ConfigButton {

    public static final Texture ICON = TexLoader.getTexture(makeUIPath("download.png"));

    public LoadConfigButton(float cX, float cY, int slotIndex) {
        super(cX, cY, ICON);
        onClick = () -> {
            if (slotIndex < 2)
                JsonConfig.setConfig(JsonConfig.defaultConfigData.get(slotIndex));
            else
                JsonConfig.loadCustomPreset(slotIndex - 2);
            return null;
        };
    }
}
