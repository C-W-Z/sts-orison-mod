package orison.core;

import static orison.core.OrisonMod.orisonExtensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import orison.core.abstracts.AbstractOrison;
import orison.core.interfaces.OrisonExtension;

public class OrisonLib {

    private static final Logger logger = LogManager.getLogger(OrisonLib.class);

    public static List<AbstractOrison> allOrisons = new ArrayList<>();
    public static HashMap<String, AbstractOrison> id2Orison = new HashMap<>();

    public static void initialize() {
        for (OrisonExtension extensions : orisonExtensions) {
            List<AbstractOrison> orisons = extensions.getAllOrisons();
            if (orisons != null)
                allOrisons.addAll(orisons);
        }
        for (AbstractOrison orison : allOrisons) {
            id2Orison.put(orison.id, orison);
            logger.info("Orison " + orison.id + " Registered!");
        }
    }

    public static AbstractOrison getOrison(String ID, boolean adv) {
        AbstractOrison orison = id2Orison.get(ID);
        if (orison == null)
            return null;
        return orison.newInstance(adv);
    }

    public static AbstractOrison getRandomCommonOrison(boolean adv) {
        List<AbstractOrison> pool = new ArrayList<>();

        for (OrisonExtension extensions : orisonExtensions)
            extensions.addCommonOrisonsToPool(pool, adv);

        pool.removeIf(Objects::isNull);
        if (pool.isEmpty())
            return getErrorOrison();
        return pool.get(AbstractDungeon.miscRng.random(pool.size() - 1)).newInstance(adv);
    }

    public static void addSpecificOrisonsToPool(List<AbstractOrison> pool, List<AbstractOrison> orisons, boolean adv) {
        for (AbstractOrison o : orisons)
            pool.add(o.newInstance(adv));
    }

    public static AbstractOrison getErrorOrison() {
        return new ErrorOrison();
    }
}
