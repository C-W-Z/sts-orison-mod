package orison.core.abstracts;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;

import orison.utils.TexLoader;

import static orison.core.OrisonMod.makeIconPath;
import static orison.utils.GeneralUtils.removePrefix;
import static orison.core.OrisonMod.makeID;

public abstract class AbstractIcon extends AbstractCustomIcon {

    protected static String getID(Class<? extends AbstractIcon> clz) {
        return makeID(clz.getSimpleName().replaceAll("Icon", ""));
    }

    public AbstractIcon(String name) {
        super(name, TexLoader.getTextureAsAtlasRegion(makeIconPath(removePrefix(name) + ".png")));
        this.region.offsetY = -2;
    }

    public abstract AbstractIcon get();
}
