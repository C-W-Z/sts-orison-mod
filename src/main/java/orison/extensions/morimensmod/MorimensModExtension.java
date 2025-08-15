package orison.extensions.morimensmod;

import static orison.core.OrisonMod.modID;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.AutoAdd;
import orison.core.OrisonLib;
import orison.core.OrisonMod;
import orison.core.abstracts.AbstractOrison;
import orison.core.interfaces.OrisonExtension;

@OrisonExtension.Initializer
public class MorimensModExtension implements OrisonExtension {

    private static final Logger logger = LogManager.getLogger(OrisonLib.class);

    public static final boolean isMorimensModLoaded;
    public static final String MorimensModID = "morimensmod";
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

    public static final String commonOrisonPackageName = "orison.extensions.morimensmod.orisons.common";
    public static final String chaosOrisonPackageName = "orison.extensions.morimensmod.orisons.chaos";
    public static final String aequorOrisonPackageName = "orison.extensions.morimensmod.orisons.aequor";
    public static final String caroOrisonPackageName = "orison.extensions.morimensmod.orisons.caro";
    public static final String ultraOrisonPackageName = "orison.extensions.morimensmod.orisons.ultra";

    public static List<AbstractOrison> allOrisons = new ArrayList<>();
    public static List<AbstractOrison> commonOrisons = new ArrayList<>();
    public static List<AbstractOrison> chaosOrisons = new ArrayList<>();
    public static List<AbstractOrison> aequorOrisons = new ArrayList<>();
    public static List<AbstractOrison> caroOrisons = new ArrayList<>();
    public static List<AbstractOrison> ultraOrisons = new ArrayList<>();

    static {
        isMorimensModLoaded = Loader.isModLoaded(MorimensModID);
        logger.info("Morimens Mod " + (isMorimensModLoaded ? "is loaded" : "is NOT loaded"));
    }

    public MorimensModExtension() {
        if (!isMorimensModLoaded)
            return;
        try {
            AbstractAwakenerClass = Class.forName(AbstractAwakenerClassName);
            getRealmColor = AbstractAwakenerClass.getMethod(AbstractAwakenerClassName);
            CHAOS_COLOR = Enum.valueOf(CardColor.class, ChaosColorName);
            AEQUOR_COLOR = Enum.valueOf(CardColor.class, AequorColorName);
            CARO_COLOR = Enum.valueOf(CardColor.class, CaroColorName);
            ULTRA_COLOR = Enum.valueOf(CardColor.class, UltraColorName);
        } catch (Exception e) {
            logger.error("Morimens Mod Loaded but some class or method NOT Found");
            e.printStackTrace();
            return;
        }

        new AutoAdd(modID)
                .packageFilter(commonOrisonPackageName)
                .any(AbstractOrison.class, (info, orison) -> {
                    allOrisons.add(orison);
                    commonOrisons.add(orison);
                });
        new AutoAdd(modID)
                .packageFilter(chaosOrisonPackageName)
                .any(AbstractOrison.class, (info, orison) -> {
                    allOrisons.add(orison);
                    chaosOrisons.add(orison);
                });
        new AutoAdd(modID)
                .packageFilter(aequorOrisonPackageName)
                .any(AbstractOrison.class, (info, orison) -> {
                    allOrisons.add(orison);
                    aequorOrisons.add(orison);
                });
        new AutoAdd(modID)
                .packageFilter(caroOrisonPackageName)
                .any(AbstractOrison.class, (info, orison) -> {
                    allOrisons.add(orison);
                    caroOrisons.add(orison);
                });
        new AutoAdd(modID)
                .packageFilter(ultraOrisonPackageName)
                .any(AbstractOrison.class, (info, orison) -> {
                    allOrisons.add(orison);
                    ultraOrisons.add(orison);
                });

        OrisonMod.register(this);
    }

    public static boolean isPlayerAwakener() {
        return isMorimensModLoaded && AbstractAwakenerClass != null && AbstractDungeon.player != null
                && AbstractAwakenerClass.isInstance(AbstractDungeon.player);
    }

    @Override
    public void addCommonOrisonsToPool(List<AbstractOrison> pool, boolean adv) {
        if (!isPlayerAwakener())
            return;

        OrisonLib.addSpecificOrisonsToPool(pool, commonOrisons, adv);

        if (getRealmColor == null)
            return;
        try {
            CardColor realmColor = (CardColor) getRealmColor.invoke(AbstractDungeon.player);
            if (realmColor == CHAOS_COLOR)
                OrisonLib.addSpecificOrisonsToPool(pool, chaosOrisons, adv);
            if (realmColor == AEQUOR_COLOR)
                OrisonLib.addSpecificOrisonsToPool(pool, aequorOrisons, adv);
            if (realmColor == CARO_COLOR)
                OrisonLib.addSpecificOrisonsToPool(pool, caroOrisons, adv);
            if (realmColor == ULTRA_COLOR)
                OrisonLib.addSpecificOrisonsToPool(pool, ultraOrisons, adv);
        } catch (Exception e) {
            logger.error("getRealmColor Error");
            e.printStackTrace();
        }
    }
}
