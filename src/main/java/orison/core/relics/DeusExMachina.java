package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import orison.core.abstracts.AbstractOrison;
import orison.core.abstracts.AbstractOrisonRelic;

/** @see AbstractOrison */
public class DeusExMachina extends AbstractOrisonRelic {

    public static final String ID = makeID(DeusExMachina.class.getSimpleName());

    public DeusExMachina() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }
}
