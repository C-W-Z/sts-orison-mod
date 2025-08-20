package orison.core.libs;

import static orison.core.OrisonMod.orisonExtensions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.random.Random;

import orison.core.abstracts.AbstractOrison;
import orison.core.interfaces.OrisonExtension;
import orison.core.savables.OrisonRng;
import orison.misc.ErrorOrison;

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
        return id2Orison.getOrDefault(ID, getErrorOrison()).newInstance(adv);
    }

    public static void addSpecificOrisonsToPool(List<AbstractOrison> pool, List<AbstractOrison> orisons, boolean adv) {
        for (AbstractOrison o : orisons)
            pool.add(o.newInstance(adv));
    }

    public static AbstractOrison getErrorOrison() {
        return new ErrorOrison();
    }

    public static List<AbstractOrison> getRandomCommonOrison(boolean adv, int amount, boolean allowDup) {
        return getRandomCommonOrison(adv, amount, allowDup, null);
    }

    public static List<AbstractOrison> getRandomCommonOrison(boolean adv, int amount, boolean allowDup, Predicate<AbstractOrison> except) {
        List<AbstractOrison> pool = new ArrayList<>();

        for (OrisonExtension extensions : orisonExtensions)
            extensions.addCommonOrisonsToPool(pool, adv);

        pool.removeIf(Objects::isNull);
        if (except != null)
            pool.removeIf(except);
        List<AbstractOrison> ret = pickRandomByRarity(pool, amount, allowDup, OrisonRng.get());
        logger.debug("getRandomCommonOrison");
        ret.forEach(o -> logger.debug("rolled: " + o.id));
        return ret;
    }

    /**
     * 從 list 中依 rarity 權重隨機抽取 n 個元素
     *
     * @param list     原始 Orison list
     * @param n        要抽的數量
     * @param allowDup 是否允許重複抽中同一個元素
     * @return 抽出的 Orison 列表
     */
    public static List<AbstractOrison> pickRandomByRarity(List<AbstractOrison> list, int n, boolean allowDup, Random random) {
        if (list == null || list.isEmpty() || n <= 0) {
            // 不能用Collections.emptyList()，因為那是不可變的List
            return new ArrayList<>();
        }

        if (!allowDup && n >= list.size()) {
            // 如果不允許重複且 n 超過 list 大小，直接返回全部打亂
            List<AbstractOrison> copy = new ArrayList<>(list);
            Collections.shuffle(copy, random.random);
            return copy;
        }

        List<AbstractOrison> result = new ArrayList<>();
        List<AbstractOrison> pool = allowDup ? list : new ArrayList<>(list);

        for (int i = 0; i < n; i++) {
            AbstractOrison picked = pickOne(pool, random);
            if (picked == null)
                break; // 權重全 0 或空池
            result.add(picked);

            if (!allowDup)
                pool.remove(picked);
        }

        return result;
    }

    /** 從 list 中依 rarity 權重抽一個元素 */
    private static AbstractOrison pickOne(List<AbstractOrison> list, Random random) {
        float totalWeight = 0;
        for (AbstractOrison o : list)
            if (o != null && o.rarity > 0)
                totalWeight += o.rarity;

        if (totalWeight <= 0)
            return null;

        float r = random.random(totalWeight);
        float cumulative = 0;
        for (AbstractOrison o : list) {
            cumulative += o.rarity;
            if (r < cumulative)
                return o;
        }
        return list.get(list.size() - 1); // 防止浮點誤差
    }
}
