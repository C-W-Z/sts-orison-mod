package orison.extensions.morimensmod.icons;

import orison.core.abstracts.AbstractIcon;

public class BloodlustIcon extends AbstractIcon {

    public static final String ID = getID(BloodlustIcon.class);
    private static BloodlustIcon singleton;

    public BloodlustIcon() {
        super(ID);
    }

    @Override
    public BloodlustIcon get() {
        if (singleton == null)
            singleton = new BloodlustIcon();
        return singleton;
    }
}
