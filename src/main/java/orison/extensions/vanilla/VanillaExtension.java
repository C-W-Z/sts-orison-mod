package orison.extensions.vanilla;

import static orison.core.OrisonMod.modID;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basemod.AutoAdd;
import orison.core.OrisonLib;
import orison.core.OrisonMod;
import orison.core.abstracts.AbstractOrison;
import orison.core.interfaces.OrisonExtension;

@OrisonExtension.Initializer
public class VanillaExtension implements OrisonExtension {

    private static final Logger logger = LogManager.getLogger(VanillaExtension.class);

    public static final String commonOrisonPackageName = "orison.extensions.vanilla.orisons.common";
    public static final String eventOrisonPackageName = "orison.extensions.vanilla.orisons.event";

    public static List<AbstractOrison> allOrisons = new ArrayList<>();
    public static List<AbstractOrison> commonOrisons = new ArrayList<>();
    public static List<AbstractOrison> eventOrisons = new ArrayList<>();

    public VanillaExtension() {
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
