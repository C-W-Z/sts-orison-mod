package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class LexGenisIcon extends AbstractIcon {

    public static final String ID = getID(LexGenisIcon.class);
    private static LexGenisIcon singleton;

    public LexGenisIcon() {
        super(ID);
    }

    @Override
    public LexGenisIcon get() {
        if (singleton == null)
            singleton = new LexGenisIcon();
        return singleton;
    }
}
