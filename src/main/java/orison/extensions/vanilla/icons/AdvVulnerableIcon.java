package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvVulnerableIcon extends AbstractIcon {

    public static final String ID = getID(AdvVulnerableIcon.class);
    private static AdvVulnerableIcon singleton;

    public AdvVulnerableIcon() {
        super(ID);
    }

    @Override
    public AdvVulnerableIcon get() {
        if (singleton == null)
            singleton = new AdvVulnerableIcon();
        return singleton;
    }
}
