package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class AdvInsightIcon extends AbstractIcon {

    public static final String ID = getID(AdvInsightIcon.class);
    private static AdvInsightIcon singleton;

    public AdvInsightIcon() {
        super(ID);
    }

    @Override
    public AdvInsightIcon get() {
        if (singleton == null)
            singleton = new AdvInsightIcon();
        return singleton;
    }
}
