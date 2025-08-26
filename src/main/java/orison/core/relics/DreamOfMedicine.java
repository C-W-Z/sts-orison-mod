package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import orison.core.abstracts.AbstractOrisonRelic;

public class DreamOfMedicine extends AbstractOrisonRelic {

    public static final String ID = makeID(DreamOfMedicine.class.getSimpleName());

    public DreamOfMedicine() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public boolean canSpawn() {
        return false;
    }
}
