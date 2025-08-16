package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class EnsembleIcon extends AbstractIcon {

    public static final String ID = getID(EnsembleIcon.class);
    private static EnsembleIcon singleton;

    public EnsembleIcon() {
        super(ID);
    }

    @Override
    public EnsembleIcon get() {
        if (singleton == null)
            singleton = new EnsembleIcon();
        return singleton;
    }
}
