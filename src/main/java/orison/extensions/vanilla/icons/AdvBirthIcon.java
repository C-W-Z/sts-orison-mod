package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvBirthIcon extends AbstractIcon {

    public static final String ID = getID(AdvBirthIcon.class);
    private static AdvBirthIcon singleton;

    public AdvBirthIcon() {
        super(ID);
    }

    @Override
    public AdvBirthIcon get() {
        if (singleton == null)
            singleton = new AdvBirthIcon();
        return singleton;
    }
}
