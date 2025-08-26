package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import orison.core.abstracts.AbstractOrisonRelic;

public class OrganicForm extends AbstractOrisonRelic {

    public static final String ID = makeID(OrganicForm.class.getSimpleName());

    public OrganicForm() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public boolean canSpawn() {
        return false;
    }
}
