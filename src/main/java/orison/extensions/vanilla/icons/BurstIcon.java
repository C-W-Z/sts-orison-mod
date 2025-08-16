package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class BurstIcon extends AbstractIcon {

    public static final String ID = getID(BurstIcon.class);
    private static BurstIcon singleton;

    public BurstIcon() {
        super(ID);
    }

    @Override
    public BurstIcon get() {
        if (singleton == null)
            singleton = new BurstIcon();
        return singleton;
    }
}
