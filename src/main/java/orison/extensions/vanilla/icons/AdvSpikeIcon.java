package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvSpikeIcon extends AbstractIcon {

    public static final String ID = getID(AdvSpikeIcon.class);
    private static AdvSpikeIcon singleton;

    public AdvSpikeIcon() {
        super(ID);
    }

    @Override
    public AdvSpikeIcon get() {
        if (singleton == null)
            singleton = new AdvSpikeIcon();
        return singleton;
    }
}
