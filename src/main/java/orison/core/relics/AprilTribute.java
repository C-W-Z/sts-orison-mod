package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import orison.core.abstracts.AbstractOrison;
import orison.core.abstracts.AbstractOrisonRelic;

/** @see AbstractOrison */
public class AprilTribute extends AbstractOrisonRelic {

    public static final String ID = makeID(AprilTribute.class.getSimpleName());

    public AprilTribute() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }
}
