package orison.core.rewards;

import static orison.core.OrisonMod.makeRewardPath;

import com.badlogic.gdx.graphics.Texture;

import orison.core.patches.RewardTypePatch;
import orison.utils.TexLoader;

public class RandomAdvOrisonReward extends RandomOrisonReward {

    public static final RewardType TYPE = RewardTypePatch.RandomAdvOrisonReward;
    private static final Texture ICON = TexLoader.getTexture(makeRewardPath(TYPE.name() + ".png"));

    public RandomAdvOrisonReward(int amount) {
        super(ICON, TYPE, amount, true);
    }
}
