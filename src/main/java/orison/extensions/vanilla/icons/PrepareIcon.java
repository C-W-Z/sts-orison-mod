package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class PrepareIcon extends AbstractIcon {

    public static final String ID = getID(PrepareIcon.class);
    private static PrepareIcon singleton;

    public PrepareIcon() {
        super(ID);
    }

    @Override
    public PrepareIcon get() {
        if (singleton == null)
            singleton = new PrepareIcon();
        return singleton;
    }
}
