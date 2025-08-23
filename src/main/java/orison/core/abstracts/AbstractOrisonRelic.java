package orison.core.abstracts;

import basemod.abstracts.CustomRelic;
import orison.utils.TexLoader;

import static orison.core.OrisonMod.makeRelicPath;
import static orison.utils.GeneralUtils.removePrefix;

import com.megacrit.cardcrawl.relics.AbstractRelic;

public abstract class AbstractOrisonRelic extends CustomRelic {

    public AbstractOrisonRelic(String id, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        super(id, TexLoader.getTexture(makeRelicPath(removePrefix(id) + ".png")), tier, sfx);
        outlineImg = TexLoader.getTexture(makeRelicPath("Empty.png"));
        largeImg = TexLoader.getTexture(makeRelicPath("large/" + removePrefix(id) + ".png"));
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
