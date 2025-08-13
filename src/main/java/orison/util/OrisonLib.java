package orison.util;

import static orison.OrisonMod.modID;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import basemod.AutoAdd;
import orison.cardmodifiers.AbstractOrison;

public class OrisonLib {

    public static HashMap<String, Class<? extends AbstractOrison>> orisons;

    public static void initialize() {
        new AutoAdd(modID)
                .packageFilter(AbstractOrison.class)
                .any(AbstractOrison.class, (info, orison) -> {
                    orisons.put(orison.id, orison.getClass());
                });
    }

    /**
     * 根據 ID 建立新的 AbstractOrison 實例
     *
     * @param ID  Orison 的 ID
     * @param adv 傳給 constructor 的參數
     */
    public static AbstractOrison getOrison(String ID, boolean adv) {
        Class<? extends AbstractOrison> clazz = orisons.get(ID);
        if (clazz == null)
            return null;

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
