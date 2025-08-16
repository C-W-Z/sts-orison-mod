package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class LexVolisIcon extends AbstractIcon {

    public static final String ID = getID(LexVolisIcon.class);
    private static LexVolisIcon singleton;

    public LexVolisIcon() {
        super(ID);
    }

    @Override
    public LexVolisIcon get() {
        if (singleton == null)
            singleton = new LexVolisIcon();
        return singleton;
    }
}
