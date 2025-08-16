package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class VulnerableIcon extends AbstractIcon {

    public static final String ID = getID(VulnerableIcon.class);
    private static VulnerableIcon singleton;

    public VulnerableIcon() {
        super(ID);
    }

    @Override
    public VulnerableIcon get() {
        if (singleton == null)
            singleton = new VulnerableIcon();
        return singleton;
    }
}
