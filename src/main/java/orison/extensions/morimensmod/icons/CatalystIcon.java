package orison.extensions.morimensmod.icons;

import orison.core.abstracts.AbstractIcon;

public class CatalystIcon extends AbstractIcon {

    public static final String ID = getID(CatalystIcon.class);
    private static CatalystIcon singleton;

    public CatalystIcon() {
        super(ID);
    }

    @Override
    public CatalystIcon get() {
        if (singleton == null)
            singleton = new CatalystIcon();
        return singleton;
    }
}
