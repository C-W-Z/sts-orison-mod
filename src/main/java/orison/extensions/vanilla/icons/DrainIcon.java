package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class DrainIcon extends AbstractIcon {

    public static final String ID = getID(DrainIcon.class);
    private static DrainIcon singleton;

    public DrainIcon() {
        super(ID);
    }

    @Override
    public DrainIcon get() {
        if (singleton == null)
            singleton = new DrainIcon();
        return singleton;
    }
}
