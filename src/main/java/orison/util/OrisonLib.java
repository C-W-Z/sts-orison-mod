package orison.util;

import static orison.OrisonMod.modID;
import static orison.OrisonMod.orisonSubcribers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.AutoAdd;
import orison.cardmodifiers.AbstractOrison;
import orison.interfaces.OrisonExtension;

public class OrisonLib {

    // public static AbstractOrison ERROR_ORISON;

    public static final List<String> eventOrisonIDs = Arrays.asList();

    public static HashMap<String, AbstractOrison> id2Orison = new HashMap<>();
    public static List<AbstractOrison> allOrisons;
    public static List<AbstractOrison> commonOrisons;

    public static void initialize() {
        new AutoAdd(modID)
                .packageFilter(AbstractOrison.class)
                .any(AbstractOrison.class, (info, orison) -> {
                    id2Orison.put(orison.id, orison);
                });
        allOrisons = new ArrayList<>(id2Orison.values());
        commonOrisons = allOrisons.stream()
                .filter(o -> !eventOrisonIDs.contains(o.id) && !o.isModedOrison)
                .collect(Collectors.toList());
    }

    public static AbstractOrison getOrison(String ID, boolean adv) {
        AbstractOrison orison = id2Orison.get(ID);
        if (orison == null)
            return null;
        return orison.newInstance(adv);
    }

    public static AbstractOrison getRandomOrison(boolean adv) {
        return allOrisons.get(AbstractDungeon.miscRng.random(allOrisons.size() - 1)).newInstance(adv);
    }

    public static AbstractOrison getRandomCommonOrison(boolean adv) {
        List<AbstractOrison> pool = new ArrayList<>(commonOrisons);

        for (OrisonExtension subscriber : orisonSubcribers)
            subscriber.addCommonOrisonsToPool(pool, adv);

        pool.removeIf(Objects::isNull);
        return pool.get(AbstractDungeon.miscRng.random(pool.size() - 1)).newInstance(adv);
    }

    public static void addSpecificOrisonsToPool(List<AbstractOrison> pool, List<AbstractOrison> orisons, boolean adv) {
        for (AbstractOrison o : orisons)
            pool.add(o.newInstance(adv));
    }
}
