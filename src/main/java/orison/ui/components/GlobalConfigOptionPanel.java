package orison.ui.components;

import static orison.core.OrisonMod.makeID;

import java.util.Map;

import com.megacrit.cardcrawl.core.CardCrawlGame;

import orison.core.configs.OrisonConfig;

public class GlobalConfigOptionPanel extends ConfigOptionPanel {

    public static Map<String, String> configTextDict = CardCrawlGame.languagePack
            .getUIString(makeID(GlobalConfigOptionPanel.class.getSimpleName())).TEXT_DICT;

    public GlobalConfigOptionPanel(float x, float rightX, float y) {
        super(x, rightX, y);
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

        init();
    }
}
