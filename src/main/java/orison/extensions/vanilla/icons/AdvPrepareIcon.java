package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvPrepareIcon extends AbstractIcon {

    public static final String ID = getID(AdvPrepareIcon.class);
    private static AdvPrepareIcon singleton;

    public AdvPrepareIcon() {
        super(ID);
    }

    @Override
    public AdvPrepareIcon get() {
        if (singleton == null)
            singleton = new AdvPrepareIcon();
        return singleton;
    }
}
