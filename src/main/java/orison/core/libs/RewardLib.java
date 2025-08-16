package orison.core.libs;

import com.megacrit.cardcrawl.rewards.RewardSave;

import basemod.BaseMod;
import orison.core.rewards.RandomOrisonReward;

public class RewardLib {

    public static void initialize() {
        BaseMod.registerCustomReward(RandomOrisonReward.TYPE,
                rewardSave -> new RandomOrisonReward(rewardSave.amount),
                customReward -> new RewardSave(customReward.type.name(), null,
                        ((RandomOrisonReward) customReward).amount, 0));
    }
}
