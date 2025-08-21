package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvFinesseIcon extends AbstractIcon {

    public static final String ID = getID(AdvFinesseIcon.class);
    private static AdvFinesseIcon singleton;

    public AdvFinesseIcon() {
        super(ID);
    }

    @Override
    public AdvFinesseIcon get() {
        if (singleton == null)
            singleton = new AdvFinesseIcon();
        return singleton;
    }
}
