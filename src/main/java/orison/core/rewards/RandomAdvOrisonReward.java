package orison.core.rewards;

import static orison.core.OrisonMod.makeID;
import static orison.core.OrisonMod.makeRewardPath;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import orison.core.patches.RewardTypePatch;
import orison.utils.TexLoader;

public class RandomAdvOrisonReward extends RandomOrisonReward {

    public static final RewardType TYPE = RewardTypePatch.RandomAdvOrisonReward;
    private static final Texture ICON = TexLoader.getTexture(makeRewardPath(TYPE.name() + ".png"));
    private static final String TEXT = CardCrawlGame.languagePack.getUIString(makeID(TYPE.name())).TEXT[0];

    public RandomAdvOrisonReward(int amount) {
        super(ICON, TEXT, TYPE, amount, true);
    }
}
