package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import orison.core.abstracts.AbstractOrison;
import orison.core.abstracts.AbstractOrisonRelic;

/** @see AbstractOrison */
public class Paradox extends AbstractOrisonRelic {

    public static final String ID = makeID(Paradox.class.getSimpleName());

    public Paradox() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public boolean canSpawn() {
        return false;
    }
}
