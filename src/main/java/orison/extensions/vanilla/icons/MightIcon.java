package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class MightIcon extends AbstractIcon {

    public static final String ID = getID(MightIcon.class);
    private static MightIcon singleton;

    public MightIcon() {
        super(ID);
    }

    @Override
    public MightIcon get() {
        if (singleton == null)
            singleton = new MightIcon();
        return singleton;
    }
}
