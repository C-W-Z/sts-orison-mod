package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvForgingIcon extends AbstractIcon {

    public static final String ID = getID(AdvForgingIcon.class);
    private static AdvForgingIcon singleton;

    public AdvForgingIcon() {
        super(ID);
    }

    @Override
    public AdvForgingIcon get() {
        if (singleton == null)
            singleton = new AdvForgingIcon();
        return singleton;
    }
}
