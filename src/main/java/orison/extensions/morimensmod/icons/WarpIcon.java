package orison.extensions.morimensmod.icons;

import orison.core.abstracts.AbstractIcon;

public class WarpIcon extends AbstractIcon {

    public static final String ID = getID(WarpIcon.class);
    private static WarpIcon singleton;

    public WarpIcon() {
        super(ID);
    }

    @Override
    public WarpIcon get() {
        if (singleton == null)
            singleton = new WarpIcon();
        return singleton;
    }
}
