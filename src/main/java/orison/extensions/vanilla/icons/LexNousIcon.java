package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class LexNousIcon extends AbstractIcon {

    public static final String ID = getID(LexNousIcon.class);
    private static LexNousIcon singleton;

    public LexNousIcon() {
        super(ID);
    }

    @Override
    public LexNousIcon get() {
        if (singleton == null)
            singleton = new LexNousIcon();
        return singleton;
    }
}
