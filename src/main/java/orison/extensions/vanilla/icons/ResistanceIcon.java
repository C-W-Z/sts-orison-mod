package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class ResistanceIcon extends AbstractIcon {

    public static final String ID = getID(ResistanceIcon.class);
    private static ResistanceIcon singleton;

    public ResistanceIcon() {
        super(ID);
    }

    @Override
    public ResistanceIcon get() {
        if (singleton == null)
            singleton = new ResistanceIcon();
        return singleton;
    }
}
