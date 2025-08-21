package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvComputationIcon extends AbstractIcon {

    public static final String ID = getID(AdvComputationIcon.class);
    private static AdvComputationIcon singleton;

    public AdvComputationIcon() {
        super(ID);
    }

    @Override
    public AdvComputationIcon get() {
        if (singleton == null)
            singleton = new AdvComputationIcon();
        return singleton;
    }
}
