package orison.extensions.morimensmod.icons;

import orison.core.abstracts.AbstractIcon;

public class DominionIcon extends AbstractIcon {

    public static final String ID = getID(DominionIcon.class);
    private static DominionIcon singleton;

    public DominionIcon() {
        super(ID);
    }

    @Override
    public DominionIcon get() {
        if (singleton == null)
            singleton = new DominionIcon();
        return singleton;
    }
}
