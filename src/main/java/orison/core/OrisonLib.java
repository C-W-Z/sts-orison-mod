package orison.core;

import static orison.core.OrisonMod.orisonExtensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;

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
        return id2Orison.getOrDefault(ID, getErrorOrison()).newInstance(adv);
    }

    public static AbstractOrison getRandomCommonOrison(boolean adv) {
        List<AbstractOrison> pool = new ArrayList<>();

        for (OrisonExtension extensions : orisonExtensions)
            extensions.addCommonOrisonsToPool(pool, adv);

        pool.removeIf(Objects::isNull);
        AbstractOrison ret = pickRandomByRarity(pool, AbstractDungeon.miscRng);
        return ret != null ? ret : getErrorOrison();
    }

    public static void addSpecificOrisonsToPool(List<AbstractOrison> pool, List<AbstractOrison> orisons, boolean adv) {
        for (AbstractOrison o : orisons)
            pool.add(o.newInstance(adv));
    }

    public static AbstractOrison getErrorOrison() {
        return new ErrorOrison();
    }

    public static AbstractOrison pickRandomByRarity(List<AbstractOrison> list, Random random) {
        if (list == null || list.isEmpty())
            return null; // 沒東西直接返回 null

        // 1. 計算總權重
        float totalWeight = 0F;
        for (AbstractOrison o : list)
            if (o != null)
                totalWeight += o.rarity;

        if (totalWeight <= 0F)
            return null; // 避免除以 0 或權重全是 0

        // 2. 生成一個 [0, totalWeight) 的隨機數
        float r = random.random(totalWeight);

        // 3. 從頭累加，找到落在哪個區間
        float cumulative = 0F;
        for (AbstractOrison o : list) {
            cumulative += o.rarity;
            if (r < cumulative)
                return o;
        }

        // 理論上不會到這，但防止浮點數誤差
        return list.get(list.size() - 1);
    }
}
