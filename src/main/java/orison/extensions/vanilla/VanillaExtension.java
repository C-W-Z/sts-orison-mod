package orison.extensions.vanilla;

import static orison.core.OrisonMod.modID;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper;

import basemod.AutoAdd;
import orison.core.OrisonMod;
import orison.core.abstracts.AbstractIcon;
import orison.core.abstracts.AbstractOrison;
import orison.core.interfaces.OrisonExtension;
import orison.core.libs.OrisonLib;

@OrisonExtension.Initializer
public class VanillaExtension implements OrisonExtension {

    private static final Logger logger = LogManager.getLogger(VanillaExtension.class);

    public static final String orisonIconPackageName = "orison.extensions.vanilla.icons";
    public static final String commonOrisonPackageName = "orison.extensions.vanilla.orisons.common";
    public static final String eventOrisonPackageName = "orison.extensions.vanilla.orisons.event";

    public static List<AbstractOrison> allOrisons = new ArrayList<>();
    public static List<AbstractOrison> commonOrisons = new ArrayList<>();
    public static List<AbstractOrison> eventOrisons = new ArrayList<>();

    public VanillaExtension() {
        // Icon一定要在Orison創建之前註冊
        new AutoAdd(modID)
                .packageFilter(orisonIconPackageName)
                .any(AbstractIcon.class, (info, icon) -> CustomIconHelper.addCustomIcon(icon.get()));
        new AutoAdd(modID)
                .packageFilter(commonOrisonPackageName)
                .any(AbstractOrison.class, (info, orison) -> {
                    allOrisons.add(orison);
                    commonOrisons.add(orison);
                });
        new AutoAdd(modID)
                .packageFilter(eventOrisonPackageName)
                .any(AbstractOrison.class, (info, orison) -> {
                    allOrisons.add(orison);
                    eventOrisons.add(orison);
                });
        OrisonMod.register(this);
    }

    @Override
    public List<AbstractOrison> getAllOrisons() {
        return allOrisons;
    }

    @Override
    public void addCommonOrisonsToPool(List<AbstractOrison> pool, boolean adv) {
        OrisonLib.addSpecificOrisonsToPool(pool, commonOrisons, adv);
    }
}
