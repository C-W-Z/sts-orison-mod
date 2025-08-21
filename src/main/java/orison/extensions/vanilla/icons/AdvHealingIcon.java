package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvHealingIcon extends AbstractIcon {

    public static final String ID = getID(AdvHealingIcon.class);
    private static AdvHealingIcon singleton;

    public AdvHealingIcon() {
        super(ID);
    }

    @Override
    public AdvHealingIcon get() {
        if (singleton == null)
            singleton = new AdvHealingIcon();
        return singleton;
    }
}
