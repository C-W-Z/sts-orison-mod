package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class ForgingIcon extends AbstractIcon {

    public static final String ID = getID(ForgingIcon.class);
    private static ForgingIcon singleton;

    public ForgingIcon() {
        super(ID);
    }

    @Override
    public ForgingIcon get() {
        if (singleton == null)
            singleton = new ForgingIcon();
        return singleton;
    }
}
