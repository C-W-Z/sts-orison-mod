package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class MirrorIcon extends AbstractIcon {

    public static final String ID = getID(MirrorIcon.class);
    private static MirrorIcon singleton;

    public MirrorIcon() {
        super(ID);
    }

    @Override
    public MirrorIcon get() {
        if (singleton == null)
            singleton = new MirrorIcon();
        return singleton;
    }
}
