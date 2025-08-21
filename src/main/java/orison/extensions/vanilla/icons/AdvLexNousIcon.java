package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvLexNousIcon extends AbstractIcon {

    public static final String ID = getID(AdvLexNousIcon.class);
    private static AdvLexNousIcon singleton;

    public AdvLexNousIcon() {
        super(ID);
    }

    @Override
    public AdvLexNousIcon get() {
        if (singleton == null)
            singleton = new AdvLexNousIcon();
        return singleton;
    }
}
