package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvBastionIcon extends AbstractIcon {

    public static final String ID = getID(AdvBastionIcon.class);
    private static AdvBastionIcon singleton;

    public AdvBastionIcon() {
        super(ID);
    }

    @Override
    public AdvBastionIcon get() {
        if (singleton == null)
            singleton = new AdvBastionIcon();
        return singleton;
    }
}
