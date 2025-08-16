package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class ToxinIcon extends AbstractIcon {

    public static final String ID = getID(ToxinIcon.class);
    private static ToxinIcon singleton;

    public ToxinIcon() {
        super(ID);
    }

    @Override
    public ToxinIcon get() {
        if (singleton == null)
            singleton = new ToxinIcon();
        return singleton;
    }
}
