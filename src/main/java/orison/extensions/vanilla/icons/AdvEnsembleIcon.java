package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvEnsembleIcon extends AbstractIcon {

    public static final String ID = getID(AdvEnsembleIcon.class);
    private static AdvEnsembleIcon singleton;

    public AdvEnsembleIcon() {
        super(ID);
    }

    @Override
    public AdvEnsembleIcon get() {
        if (singleton == null)
            singleton = new AdvEnsembleIcon();
        return singleton;
    }
}
