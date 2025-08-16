package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class WeaknessIcon extends AbstractIcon {

    public static final String ID = getID(WeaknessIcon.class);
    private static WeaknessIcon singleton;

    public WeaknessIcon() {
        super(ID);
    }

    @Override
    public WeaknessIcon get() {
        if (singleton == null)
            singleton = new WeaknessIcon();
        return singleton;
    }
}
