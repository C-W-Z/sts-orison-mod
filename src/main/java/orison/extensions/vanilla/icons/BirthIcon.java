package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class BirthIcon extends AbstractIcon {

    public static final String ID = getID(BirthIcon.class);
    private static BirthIcon singleton;

    public BirthIcon() {
        super(ID);
    }

    @Override
    public BirthIcon get() {
        if (singleton == null)
            singleton = new BirthIcon();
        return singleton;
    }
}
