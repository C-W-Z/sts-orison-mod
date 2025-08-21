package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvDrainIcon extends AbstractIcon {

    public static final String ID = getID(AdvDrainIcon.class);
    private static AdvDrainIcon singleton;

    public AdvDrainIcon() {
        super(ID);
    }

    @Override
    public AdvDrainIcon get() {
        if (singleton == null)
            singleton = new AdvDrainIcon();
        return singleton;
    }
}
