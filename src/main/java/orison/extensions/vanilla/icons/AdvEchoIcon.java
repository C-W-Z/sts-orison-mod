package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvEchoIcon extends AbstractIcon {

    public static final String ID = getID(AdvEchoIcon.class);
    private static AdvEchoIcon singleton;

    public AdvEchoIcon() {
        super(ID);
    }

    @Override
    public AdvEchoIcon get() {
        if (singleton == null)
            singleton = new AdvEchoIcon();
        return singleton;
    }
}
