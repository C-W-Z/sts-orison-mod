package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import orison.core.abstracts.AbstractOrisonRelic;
import orison.core.patches.OrisonRewardPatch;

/** @see OrisonRewardPatch */
public class ReEvolution extends AbstractOrisonRelic {

    public static final String ID = makeID(ReEvolution.class.getSimpleName());

    public ReEvolution() {
        super(ID, RelicTier.BOSS, LandingSound.FLAT);
    }
}
