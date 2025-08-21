package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvToxinIcon extends AbstractIcon {

    public static final String ID = getID(AdvToxinIcon.class);
    private static AdvToxinIcon singleton;

    public AdvToxinIcon() {
        super(ID);
    }

    @Override
    public AdvToxinIcon get() {
        if (singleton == null)
            singleton = new AdvToxinIcon();
        return singleton;
    }
}
