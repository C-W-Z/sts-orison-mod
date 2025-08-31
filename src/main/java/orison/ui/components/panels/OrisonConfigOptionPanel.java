package orison.ui.components.panels;

import static orison.core.OrisonMod.makeID;

import java.util.Map;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import orison.core.abstracts.AbstractOrison;
import orison.core.abstracts.AbstractOrison.UseType;
import orison.core.configs.OrisonConfig;
import orison.ui.components.TextConfig;

public class OrisonConfigOptionPanel extends ConfigOptionPanel {

    public static Map<String, String> configTextDict = CardCrawlGame.languagePack
            .getUIString(makeID(OrisonConfigOptionPanel.class.getSimpleName())).TEXT_DICT;

    public OrisonConfigOptionPanel(float x, float rightX, float y, AbstractOrison orison) {
        super(x, rightX, y);

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.OrisonRarity.class.getSimpleName()),
                orison.getRarity(),
                0F, 2F, 1, false,
                v -> orison.saveRarity(v))
                .setBg(false).setShowOption(() -> !orison.adv));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.OrisonUseType.class.getSimpleName()),
                orison).setBg(false).setShowOption(() -> orison.canSetUseType()));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.OrisonUseLimit.class.getSimpleName()),
                orison.getUseLimit(),
                1, 10, 0, false,
                v -> orison.saveUseLimit(MathUtils.round(v)))
                .setBg(false)
                .setShowOption(() -> orison.canSetUseLimit() && orison.getUseType() != UseType.INFINITE));

        for (int i = 0; i < orison.getValueCount(); i++) {
            final int index = i;
            addOption(new TextConfig(x, rightX, nextYPos,
                    String.format(configTextDict.get(OrisonConfig.OrisonValues.class.getSimpleName()), index + 1),
                    orison.getValue(index),
                    orison.getValueMinForConfig(index), orison.getValueMaxForConfig(index),
                    0, false,
                    v -> orison.saveValue(index, MathUtils.round(v)))
                    .setBg(false));
        }
    }
}
