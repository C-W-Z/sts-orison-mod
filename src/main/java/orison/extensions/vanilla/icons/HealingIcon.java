package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class HealingIcon extends AbstractIcon {

    public static final String ID = getID(HealingIcon.class);
    private static HealingIcon singleton;

    public HealingIcon() {
        super(ID);
    }

    @Override
    public HealingIcon get() {
        if (singleton == null)
            singleton = new HealingIcon();
        return singleton;
    }
}
