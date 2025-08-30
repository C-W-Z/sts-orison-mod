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
                .setBg(false));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.OrisonUseType.class.getSimpleName()),
                orison).setBg(false));

        addOption(new TextConfig(x, rightX, nextYPos,
                configTextDict.get(OrisonConfig.OrisonUseLimit.class.getSimpleName()),
                orison.getUseLimit(),
                0, 10, 0, false,
                v -> orison.saveUseLimit(MathUtils.round(v)))
                .setBg(false)
                .setShowOption(() -> orison.getUseType() != UseType.INFINITE));
    }
}
