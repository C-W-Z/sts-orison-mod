package orison.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

import orison.core.interfaces.ConfigUIElement;

public class ConfigUIs implements ConfigUIElement {

    private static float LINE_SPACING = Settings.BIG_TEXT_MODE ? (40.0F * Settings.scale) : (32.0F * Settings.scale);

    public List<ConfigUIElement> scrollables;
    private float realHeight;

    public static final List<String> descriptions = Arrays.asList(
            "The chance of normal Monsters dropping Orisons",
            "The chance of Elites dropping Orisons");

    public static final List<Supplier<Float>> values = Arrays.asList(
            () -> 0.25F,
            () -> 1F);

    public static final List<Consumer<Float>> onChanges = Arrays.asList(
            v -> {
            },
            v -> {
            });

    public ConfigUIs(float y) {
        float height = 0;
        scrollables = new ArrayList<>();
        for (int i = 0; i < descriptions.size(); i++) {
            scrollables.add(new ConfigSlider(
                    y - height - i * LINE_SPACING,
                    descriptions.get(i),
                    values.get(i).get(),
                    onChanges.get(i)));
            height += scrollables.get(i).getHeight();

        }
        realHeight = height + (scrollables.size() - 1) * LINE_SPACING;
    }

    @Override
    public void setTargetY(float targetY) {
        float height = 0;
        for (int i = 0; i < scrollables.size(); i++) {
            scrollables.get(i).setTargetY(targetY - height - i * LINE_SPACING);
            height += scrollables.get(i).getHeight();
        }
    }

    @Override
    public void update() {
        for (ConfigUIElement ui : scrollables)
            ui.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        for (ConfigUIElement ui : scrollables)
            ui.render(sb);
    }

    @Override
    public float getHeight() {
        return realHeight;
    }
}
