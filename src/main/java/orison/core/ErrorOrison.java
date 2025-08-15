package orison.core;

import static orison.core.OrisonMod.makeID;

import basemod.AutoAdd;
import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.core.abstracts.AbstractOrison;

@AutoAdd.Ignore
@SaveIgnore
public class ErrorOrison extends AbstractOrison {

    public static final String ID = makeID(ErrorOrison.class.getSimpleName());

    public ErrorOrison() {
        this(false);
    }

    public ErrorOrison(boolean adv) {
        super(ID, false, false, false);
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new ErrorOrison(adv);
    }

    @Override
    public String getTitle() {
        return "Error";
    }

    @Override
    public String getDescription() {
        return "Something wrong to load the Orison.";
    }
}
