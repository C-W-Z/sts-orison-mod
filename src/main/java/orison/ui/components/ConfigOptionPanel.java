package orison.ui.components;

import static orison.core.OrisonMod.makeID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import orison.core.configs.OrisonConfig;
import orison.core.interfaces.ConfigUIElement;

public class ConfigOptionPanel implements ConfigUIElement {

    public static Map<String, String> configTextDict = CardCrawlGame.languagePack
            .getUIString(makeID(ConfigOptionPanel.class.getSimpleName())).TEXT_DICT;

    private static float LINE_SPACING = Settings.BIG_TEXT_MODE ? (40.0F * Settings.scale) : (32.0F * Settings.scale);

    public List<ConfigUIElement> scrollables;
    private float realHeight;
    private float nextYPos;

    private float x; // 左側x座標
    private float rightX; // 右側x座標
    private float y; // 底部y座標

    public ConfigOptionPanel(float x, float rightX, float y) {
        this.x = x;
        this.rightX = rightX;
        nextYPos = y;
        scrollables = new ArrayList<>();

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Orison.ID_CAN_ATTACH_ON_UNPLAYABLE_CARD),
                OrisonConfig.Orison.CAN_ATTACH_ON_UNPLAYABLE_CARD,
                v -> OrisonConfig.Orison.save(OrisonConfig.Orison.ID_CAN_ATTACH_ON_UNPLAYABLE_CARD, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Orison.ID_CAN_ATTACH_ON_STATUS),
                OrisonConfig.Orison.CAN_ATTACH_ON_STATUS,
                v -> OrisonConfig.Orison.save(OrisonConfig.Orison.ID_CAN_ATTACH_ON_STATUS, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Orison.ID_CAN_ATTACH_ON_CURSE),
                OrisonConfig.Orison.CAN_ATTACH_ON_CURSE,
                v -> OrisonConfig.Orison.save(OrisonConfig.Orison.ID_CAN_ATTACH_ON_CURSE, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Orison.ID_CAN_ATTACH_ON_COLORLESS),
                OrisonConfig.Orison.CAN_ATTACH_ON_COLORLESS,
                v -> OrisonConfig.Orison.save(OrisonConfig.Orison.ID_CAN_ATTACH_ON_COLORLESS, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_MONSTER_DROP_ORISON_CHANCE),
                OrisonConfig.Reward.MONSTER_DROP_ORISON_CHANCE,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_MONSTER_DROP_ORISON_CHANCE, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_MONSTER_DROP_ORISON_ADV_CHANCE),
                OrisonConfig.Reward.MONSTER_DROP_ORISON_ADV_CHANCE,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_MONSTER_DROP_ORISON_ADV_CHANCE, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_MONSTER_DROP_ORISON_LINKED),
                OrisonConfig.Reward.MONSTER_DROP_ORISON_LINKED,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_MONSTER_DROP_ORISON_LINKED, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_ELITE_DROP_ORISON_CHANCE),
                OrisonConfig.Reward.ELITE_DROP_ORISON_CHANCE,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_ELITE_DROP_ORISON_CHANCE, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_ELITE_DROP_ORISON_ADV_CHANCE),
                OrisonConfig.Reward.ELITE_DROP_ORISON_ADV_CHANCE,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_ELITE_DROP_ORISON_ADV_CHANCE, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_ELITE_DROP_ORISON_LINKED),
                OrisonConfig.Reward.ELITE_DROP_ORISON_LINKED,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_ELITE_DROP_ORISON_LINKED, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_BOSS_DROP_ORISON_CHANCE),
                OrisonConfig.Reward.BOSS_DROP_ORISON_CHANCE,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_BOSS_DROP_ORISON_CHANCE, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_BOSS_DROP_ORISON_ADV_CHANCE),
                OrisonConfig.Reward.BOSS_DROP_ORISON_ADV_CHANCE,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_BOSS_DROP_ORISON_ADV_CHANCE, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_BOSS_DROP_ORISON_LINKED),
                OrisonConfig.Reward.BOSS_DROP_ORISON_LINKED,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_BOSS_DROP_ORISON_LINKED, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_TREASURE_DROP_ORISON_CHANCE),
                OrisonConfig.Reward.TREASURE_DROP_ORISON_CHANCE,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_TREASURE_DROP_ORISON_CHANCE, v)));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_TREASURE_DROP_ORISON_ADV_CHANCE),
                OrisonConfig.Reward.TREASURE_DROP_ORISON_ADV_CHANCE,
                v -> OrisonConfig.Reward.save(OrisonConfig.Reward.ID_TREASURE_DROP_ORISON_ADV_CHANCE, v)));

        this.y = y - realHeight + scrollables.get(0).getHeight() / 2;
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
        this.y = targetY - realHeight + scrollables.get(0).getHeight() / 2;
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

        if (Settings.isDebug || Settings.isInfo) {
            sb.setColor(Color.RED);
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG,
                    this.x, this.y,
                    rightX - x, realHeight);
        }
    }

    @Override
    public float getHeight() {
        return realHeight;
    }
}
