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

    public static List<AbstractOrison> commonOrisons = new ArrayList<>();

    public VanillaExtension() {
        OrisonMod.subscribe(this);
        logger.error("Vanilla Extension Subscribed!");
    }

    @Override
    public void registerOrisons() {
        new AutoAdd(modID)
                .packageFilter("orison.extensions.vanilla.orisons.common")
                .any(AbstractOrison.class, (info, orison) -> {
                    commonOrisons.add(orison);
                    OrisonLib.register(orison);
                });
    }

    @Override
    public void addCommonOrisonsToPool(List<AbstractOrison> pool, boolean adv) {

    }
}
