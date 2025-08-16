package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class EchoIcon extends AbstractIcon {

    public static final String ID = getID(EchoIcon.class);
    private static EchoIcon singleton;

    public EchoIcon() {
        super(ID);
    }

    @Override
    public EchoIcon get() {
        if (singleton == null)
            singleton = new EchoIcon();
        return singleton;
    }
}
