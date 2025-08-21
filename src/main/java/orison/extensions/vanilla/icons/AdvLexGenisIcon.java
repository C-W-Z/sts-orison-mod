package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvLexGenisIcon extends AbstractIcon {

    public static final String ID = getID(AdvLexGenisIcon.class);
    private static AdvLexGenisIcon singleton;

    public AdvLexGenisIcon() {
        super(ID);
    }

    @Override
    public AdvLexGenisIcon get() {
        if (singleton == null)
            singleton = new AdvLexGenisIcon();
        return singleton;
    }
}
