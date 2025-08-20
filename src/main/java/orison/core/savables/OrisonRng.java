package orison.core.savables;

import static orison.core.OrisonMod.makeID;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.random.Random;

import basemod.abstracts.CustomSavable;

public class OrisonRng implements CustomSavable<Integer> {

    public static final String ID = makeID(OrisonRng.class.getSimpleName());

    private static Random rng = null;

    private static Integer counterRemembered = null;

    public static Random get() {
        if (rng == null)
            rng = new Random(Settings.seed);
        return rng;
    }

    public static void rememberCounter() {
        counterRemembered = get().counter;
    }

    // called in OrisonMod.java
    public static void onPostDungeonInitialize() {
        rng = new Random(Settings.seed);
    }

    @Override
    public void onLoad(Integer counter) {
        if (counter == null)
            rng = new Random(Settings.seed);
        else
            rng = new Random(Settings.seed, counter);
    }

    @Override
    public Integer onSave() {
        if (counterRemembered != null) {
            int value = counterRemembered.intValue();
            counterRemembered = null;
            return value;
        }
        return get().counter;
    }
}
