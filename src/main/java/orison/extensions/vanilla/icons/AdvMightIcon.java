package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvMightIcon extends AbstractIcon {

    public static final String ID = getID(AdvMightIcon.class);
    private static AdvMightIcon singleton;

    public AdvMightIcon() {
        super(ID);
    }

    @Override
    public AdvMightIcon get() {
        if (singleton == null)
            singleton = new AdvMightIcon();
        return singleton;
    }
}
