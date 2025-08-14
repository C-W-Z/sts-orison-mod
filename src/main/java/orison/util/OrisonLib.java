package orison.util;

import static orison.OrisonMod.isMorimensModLoaded;
import static orison.OrisonMod.modID;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.AutoAdd;
import orison.cardmodifiers.AbstractOrison;

public class OrisonLib {

    private static final Logger logger = LogManager.getLogger(OrisonLib.class);

    public static final String AbstractAwakenerClassName = "morimensmod.characters.AbstractAwakener";
    public static final String getRealmColorMethodName = "getRealmColor";
    public static final String ChaosColorName = "CHAOS_COLOR";
    public static final String AequorColorName = "AEQUOR_COLOR";
    public static final String CaroColorName = "CARO_COLOR";
    public static final String UltraColorName = "ULTRA_COLOR";
    public static Class<?> AbstractAwakenerClass;
    public static Method getRealmColor;
    public static CardColor CHAOS_COLOR;
    public static CardColor AEQUOR_COLOR;
    public static CardColor CARO_COLOR;
    public static CardColor ULTRA_COLOR;

    // public static AbstractOrison ERROR_ORISON;

    public static final List<String> eventOrisonIDs = Arrays.asList();
    public static final List<String> commonAwakenerOrisonIDs = Arrays.asList();
    public static final List<String> chaosOrisonIDs = Arrays.asList();
    public static final List<String> aequorOrisonIDs = Arrays.asList();
    public static final List<String> caroOrisonIDs = Arrays.asList();
    public static final List<String> ultraOrisonIDs = Arrays.asList();
    public static final List<String> awakenerOrisonIDs = Stream
            .of(commonAwakenerOrisonIDs, chaosOrisonIDs, aequorOrisonIDs, caroOrisonIDs, ultraOrisonIDs)
            .flatMap(List::stream)
            .collect(Collectors.toList());

    public static HashMap<String, AbstractOrison> id2Orison = new HashMap<>();
    public static List<AbstractOrison> allOrisons;
    public static List<AbstractOrison> commonOrisons;

    public static void initialize() {
        if (isMorimensModLoaded) {
            try {
                AbstractAwakenerClass   = Class.forName(AbstractAwakenerClassName);
                getRealmColor           = AbstractAwakenerClass.getMethod(AbstractAwakenerClassName);
                CHAOS_COLOR             = Enum.valueOf(CardColor.class, ChaosColorName);
                AEQUOR_COLOR            = Enum.valueOf(CardColor.class, AequorColorName);
                CARO_COLOR              = Enum.valueOf(CardColor.class, CaroColorName);
                ULTRA_COLOR             = Enum.valueOf(CardColor.class, UltraColorName);
            } catch (Exception e) {
                logger.error("Morimens Mod Loaded but some class or method NOT Found");
            }
        }

        new AutoAdd(modID)
                .packageFilter(AbstractOrison.class)
                .any(AbstractOrison.class, (info, orison) -> {
                    id2Orison.put(orison.id, orison);
                });
        allOrisons = new ArrayList<>(id2Orison.values());
        commonOrisons = allOrisons.stream()
                .filter(o -> !eventOrisonIDs.contains(o.id) && !awakenerOrisonIDs.contains(o.id))
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
        if (!isPlayerAwakener())
            return commonOrisons.get(AbstractDungeon.miscRng.random(commonOrisons.size() - 1)).newInstance(adv);

        List<AbstractOrison> pool = new ArrayList<>(commonOrisons);
        addSpecificOrisonsToPool(pool, commonAwakenerOrisonIDs, adv);

        if (getRealmColor != null) {
            try {
                CardColor realmColor = (CardColor) getRealmColor.invoke(AbstractDungeon.player);
                if (realmColor == CHAOS_COLOR)
                    addSpecificOrisonsToPool(pool, chaosOrisonIDs, adv);
                if (realmColor == AEQUOR_COLOR)
                    addSpecificOrisonsToPool(pool, aequorOrisonIDs, adv);
                if (realmColor == CARO_COLOR)
                    addSpecificOrisonsToPool(pool, caroOrisonIDs, adv);
                if (realmColor == ULTRA_COLOR)
                    addSpecificOrisonsToPool(pool, ultraOrisonIDs, adv);
            } catch (Exception e) {
                logger.error("getRealmColor Error");
            }
        }

        pool.removeIf(Objects::isNull);
        return pool.get(AbstractDungeon.miscRng.random(pool.size() - 1)).newInstance(adv);
    }

    public static boolean isPlayerAwakener() {
        return isMorimensModLoaded && AbstractAwakenerClass != null && AbstractDungeon.player != null
                && AbstractAwakenerClass.isInstance(AbstractDungeon.player);
    }

    public static void addSpecificOrisonsToPool(List<AbstractOrison> pool, List<String> orisonIDs, boolean adv) {
        for (String id : orisonIDs)
            pool.add(getOrison(id, adv));
        // orisonIDs.stream().map(id -> getOrison(id, adv)).forEach(pool::add);
    }
}
