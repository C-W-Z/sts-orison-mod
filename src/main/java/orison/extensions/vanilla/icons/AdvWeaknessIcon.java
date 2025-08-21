package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvWeaknessIcon extends AbstractIcon {

    public static final String ID = getID(AdvWeaknessIcon.class);
    private static AdvWeaknessIcon singleton;

    public AdvWeaknessIcon() {
        super(ID);
    }

    @Override
    public AdvWeaknessIcon get() {
        if (singleton == null)
            singleton = new AdvWeaknessIcon();
        return singleton;
    }
}
