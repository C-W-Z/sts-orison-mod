package orison.extensions.vanilla.icons;

import orison.core.abstracts.AbstractIcon;

public class InsightIcon extends AbstractIcon {

    public static final String ID = getID(InsightIcon.class);
    private static InsightIcon singleton;

    public InsightIcon() {
        super(ID);
    }

    @Override
    public InsightIcon get() {
        if (singleton == null)
            singleton = new InsightIcon();
        return singleton;
    }
}
