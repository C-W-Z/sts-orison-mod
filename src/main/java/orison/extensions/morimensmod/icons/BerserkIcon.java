package orison.extensions.morimensmod.icons;

import orison.core.abstracts.AbstractIcon;

public class BerserkIcon extends AbstractIcon {

    public static final String ID = getID(BerserkIcon.class);
    private static BerserkIcon singleton;

    public BerserkIcon() {
        super(ID);
    }

    @Override
    public BerserkIcon get() {
        if (singleton == null)
            singleton = new BerserkIcon();
        return singleton;
    }
}
