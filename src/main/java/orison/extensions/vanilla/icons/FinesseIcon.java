package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class FinesseIcon extends AbstractIcon {

    public static final String ID = getID(FinesseIcon.class);
    private static FinesseIcon singleton;

    public FinesseIcon() {
        super(ID);
    }

    @Override
    public FinesseIcon get() {
        if (singleton == null)
            singleton = new FinesseIcon();
        return singleton;
    }
}
