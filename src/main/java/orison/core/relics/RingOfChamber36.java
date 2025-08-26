package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import orison.core.abstracts.AbstractOrisonRelic;

public class RingOfChamber36 extends AbstractOrisonRelic {

    public static final String ID = makeID(RingOfChamber36.class.getSimpleName());

    public RingOfChamber36() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public boolean canSpawn() {
        return false;
    }
}
