package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvResistanceIcon extends AbstractIcon {

    public static final String ID = getID(AdvResistanceIcon.class);
    private static AdvResistanceIcon singleton;

    public AdvResistanceIcon() {
        super(ID);
    }

    @Override
    public AdvResistanceIcon get() {
        if (singleton == null)
            singleton = new AdvResistanceIcon();
        return singleton;
    }
}
