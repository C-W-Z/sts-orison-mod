package orison.ui.components;

import static orison.core.OrisonMod.makeID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;

import orison.core.configs.OrisonConfig;
import orison.core.interfaces.ConfigUIElement;

public class ConfigOptionPanel implements ConfigUIElement {

    public static Map<String, String> configTextDict = CardCrawlGame.languagePack
            .getUIString(makeID(ConfigOptionPanel.class.getSimpleName())).TEXT_DICT;

    private static float LINE_SPACING = Settings.BIG_TEXT_MODE ? (40.0F * Settings.scale) : (32.0F * Settings.scale);

    public List<ConfigUIElement> scrollables;
    private float realHeight;
    private float nextYPos;

    public ConfigOptionPanel(float y) {
        nextYPos = y;
        scrollables = new ArrayList<>();

        addOption(new ConfigSlider(nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_MONSTER_DROP_ORISON_CHANCE),
                OrisonConfig.Reward.MONSTER_DROP_ORISON_CHANCE,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_MONSTER_DROP_ORISON_CHANCE, v)));
        addOption(new ConfigSlider(nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_MONSTER_DROP_ORISON_ADV_CHANCE),
                OrisonConfig.Reward.MONSTER_DROP_ORISON_ADV_CHANCE,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_MONSTER_DROP_ORISON_ADV_CHANCE, v)));

        addOption(new ConfigSlider(nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_ELITE_DROP_ORISON_CHANCE),
                OrisonConfig.Reward.ELITE_DROP_ORISON_CHANCE,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_ELITE_DROP_ORISON_CHANCE, v)));

        addOption(new ConfigSlider(nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_ELITE_DROP_ORISON_ADV_CHANCE),
                OrisonConfig.Reward.ELITE_DROP_ORISON_ADV_CHANCE,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_ELITE_DROP_ORISON_ADV_CHANCE, v)));
    }

    private void addOption(ConfigUIElement element) {
        scrollables.add(element);
        float elementHeight = element.getHeight();
        nextYPos -= elementHeight + LINE_SPACING;
        if (scrollables.size() > 1)
            realHeight += LINE_SPACING;
        realHeight += elementHeight;
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
