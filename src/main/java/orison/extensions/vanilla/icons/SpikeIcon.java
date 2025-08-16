package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class SpikeIcon extends AbstractIcon {

    public static final String ID = getID(SpikeIcon.class);
    private static SpikeIcon singleton;

    public SpikeIcon() {
        super(ID);
    }

    @Override
    public SpikeIcon get() {
        if (singleton == null)
            singleton = new SpikeIcon();
        return singleton;
    }
}
