package orison.core.libs;

import com.megacrit.cardcrawl.rewards.RewardSave;

import basemod.BaseMod;
import orison.core.rewards.RandomAdvOrisonReward;
import orison.core.rewards.RandomOrisonReward;

public class RewardLib {

    public static void initialize() {
        // Seems no need to register this, since we only (maybe) generate orison reward after saving
        BaseMod.registerCustomReward(RandomOrisonReward.TYPE,
                rewardSave -> new RandomOrisonReward(rewardSave.amount),
                customReward -> new RewardSave(customReward.type.name(), null,
                        ((RandomOrisonReward) customReward).amount, 0));

        BaseMod.registerCustomReward(RandomAdvOrisonReward.TYPE,
                rewardSave -> new RandomAdvOrisonReward(rewardSave.amount),
                customReward -> new RewardSave(customReward.type.name(), null,
                        ((RandomAdvOrisonReward) customReward).amount, 0));
    }
}
