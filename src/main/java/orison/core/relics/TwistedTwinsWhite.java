package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import orison.core.abstracts.AbstractOrisonRelic;
import orison.core.patches.OrisonRewardPatch;

/** @see OrisonRewardPatch */
public class TwistedTwinsWhite extends AbstractOrisonRelic {

    public static final String ID = makeID(TwistedTwinsWhite.class.getSimpleName());

    public TwistedTwinsWhite() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }
}
