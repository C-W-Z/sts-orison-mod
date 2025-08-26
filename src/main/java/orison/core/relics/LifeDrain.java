package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import orison.core.abstracts.AbstractOrisonRelic;

public class LifeDrain extends AbstractOrisonRelic {

    public static final String ID = makeID(LifeDrain.class.getSimpleName());

    public LifeDrain() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public boolean canSpawn() {
        return false;
    }
}
