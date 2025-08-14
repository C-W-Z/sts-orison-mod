package orison.util;

import static orison.OrisonMod.modID;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.AutoAdd;
import orison.cardmodifiers.AbstractOrison;

public class OrisonLib {

    public static HashMap<String, Class<? extends AbstractOrison>> id2OrisonClass;
    public static List<Class<? extends AbstractOrison>> orisonClassList;

    public static void initialize() {
        new AutoAdd(modID)
                .packageFilter(AbstractOrison.class)
                .any(AbstractOrison.class, (info, orison) -> {
                    id2OrisonClass.put(orison.id, orison.getClass());
                });
        orisonClassList = new ArrayList<>(id2OrisonClass.values());
    }

    /**
     * 根據 ID 建立新的 AbstractOrison 實例
     *
     * @param ID  Orison 的 ID
     * @param adv 傳給 constructor 的參數
     */
    public static AbstractOrison getOrison(String ID, boolean adv) {
        Class<? extends AbstractOrison> clazz = id2OrisonClass.get(ID);
        if (clazz == null)
            return null;
        return getOrisonByClass(clazz, adv);
    }

    public static AbstractOrison getRandomOrison(boolean adv) {
        Class<? extends AbstractOrison> clazz = orisonClassList
                .get(AbstractDungeon.cardRandomRng.random(orisonClassList.size() - 1));
        return getOrisonByClass(clazz, adv);
    }

    private static AbstractOrison getOrisonByClass(Class<? extends AbstractOrison> clazz, boolean adv) {
        try {
            // 找到有 boolean 參數的 constructor
            Constructor<? extends AbstractOrison> constructor = clazz.getConstructor(boolean.class);
            return constructor.newInstance(adv);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
