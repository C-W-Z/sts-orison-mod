package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvMirrorIcon extends AbstractIcon {

    public static final String ID = getID(AdvMirrorIcon.class);
    private static AdvMirrorIcon singleton;

    public AdvMirrorIcon() {
        super(ID);
    }

    @Override
    public AdvMirrorIcon get() {
        if (singleton == null)
            singleton = new AdvMirrorIcon();
        return singleton;
    }
}
