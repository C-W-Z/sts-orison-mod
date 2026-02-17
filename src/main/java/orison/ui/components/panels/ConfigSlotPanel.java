package orison.ui.components.panels;

import static orison.core.OrisonMod.makeID;

import java.util.Map;

import com.megacrit.cardcrawl.core.CardCrawlGame;

import orison.core.configs.OrisonConfig;
import orison.ui.components.TextConfig;

public class ConfigSlotPanel extends ConfigOptionPanel {

    public static Map<String, String> configTextDict = CardCrawlGame.languagePack
            .getUIString(makeID(GlobalConfigOptionPanel.class.getSimpleName())).TEXT_DICT;

    public static final float rightPadding = 10f;

    public ConfigSlotPanel(float x, float rightX, float y) {
        super(x, rightX, y);

        addOption(new TextConfig(x, rightX - rightPadding, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_TREASURE_DROP_ORISON_ADV_CHANCE),
                "LoadConfigButton", 0));

        addOption(new TextConfig(x, rightX - rightPadding, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_TREASURE_DROP_ORISON_ADV_CHANCE),
                "LoadConfigButton", 1));

        addOption(new TextConfig(x, rightX - rightPadding, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_TREASURE_DROP_ORISON_ADV_CHANCE),
                "SaveConfigButton", 0));
        addOption(new TextConfig(x, rightX - rightPadding, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_TREASURE_DROP_ORISON_ADV_CHANCE),
                "LoadConfigButton", 2));

        addOption(new TextConfig(x, rightX - rightPadding, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_TREASURE_DROP_ORISON_ADV_CHANCE),
                "SaveConfigButton", 1));
        addOption(new TextConfig(x, rightX - rightPadding, nextYPos,
                configTextDict.get(OrisonConfig.Reward.ID_TREASURE_DROP_ORISON_ADV_CHANCE),
                "LoadConfigButton", 3));

        init();
    }
}
