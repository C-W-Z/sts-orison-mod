package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvBurstIcon extends AbstractIcon {

    public static final String ID = getID(AdvBurstIcon.class);
    private static AdvBurstIcon singleton;

    public AdvBurstIcon() {
        super(ID);
    }

    @Override
    public AdvBurstIcon get() {
        if (singleton == null)
            singleton = new AdvBurstIcon();
        return singleton;
    }
}
