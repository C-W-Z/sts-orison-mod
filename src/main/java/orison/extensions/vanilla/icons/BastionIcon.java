package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class BastionIcon extends AbstractIcon {

    public static final String ID = getID(BastionIcon.class);
    private static BastionIcon singleton;

    public BastionIcon() {
        super(ID);
    }

    @Override
    public BastionIcon get() {
        if (singleton == null)
            singleton = new BastionIcon();
        return singleton;
    }
}
