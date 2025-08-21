package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvLexVolisIcon extends AbstractIcon {

    public static final String ID = getID(AdvLexVolisIcon.class);
    private static AdvLexVolisIcon singleton;

    public AdvLexVolisIcon() {
        super(ID);
    }

    @Override
    public AdvLexVolisIcon get() {
        if (singleton == null)
            singleton = new AdvLexVolisIcon();
        return singleton;
    }
}
