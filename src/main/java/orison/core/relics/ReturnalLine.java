package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import orison.core.abstracts.AbstractOrisonRelic;

public class ReturnalLine extends AbstractOrisonRelic {

    public static final String ID = makeID(ReturnalLine.class.getSimpleName());

    public ReturnalLine() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public boolean canSpawn() {
        return false;
    }
}
