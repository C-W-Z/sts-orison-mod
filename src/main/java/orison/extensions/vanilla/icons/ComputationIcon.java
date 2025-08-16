package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class ComputationIcon extends AbstractIcon {

    public static final String ID = getID(ComputationIcon.class);
    private static ComputationIcon singleton;

    public ComputationIcon() {
        super(ID);
    }

    @Override
    public ComputationIcon get() {
        if (singleton == null)
            singleton = new ComputationIcon();
        return singleton;
    }
}
