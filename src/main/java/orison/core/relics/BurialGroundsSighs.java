package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import orison.core.abstracts.AbstractOrisonRelic;
import orison.core.configs.OrisonConfig;
import orison.core.patches.OrisonRewardPatch;

/** @see OrisonRewardPatch.CombatRewardScreenPatch */
public class BurialGroundsSighs extends AbstractOrisonRelic {

    public static final String ID = makeID(BurialGroundsSighs.class.getSimpleName());

    public BurialGroundsSighs() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public boolean canSpawn() {
        return (OrisonConfig.Reward.MONSTER_DROP_ORISON_CHANCE < 1F ||
                OrisonConfig.Reward.ELITE_DROP_ORISON_CHANCE < 1F ||
                OrisonConfig.Reward.BOSS_DROP_ORISON_CHANCE < 1F);
    }
}
