package orison.core.rewards;

import static orison.core.OrisonMod.makeID;
import static orison.core.OrisonMod.makeRewardPath;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import orison.core.abstracts.AbstractOrisonReward;
import orison.core.libs.OrisonLib;
import orison.core.patches.RewardTypePatch;
import orison.utils.TexLoader;

public class RandomOrisonReward extends AbstractOrisonReward {

    public static final RewardType TYPE = RewardTypePatch.RandomOrisonReward;
    private static final Texture ICON = TexLoader.getTexture(makeRewardPath(TYPE.name() + ".png"));
    private static final String TEXT = CardCrawlGame.languagePack.getUIString(makeID(TYPE.name())).TEXT[0];

    public int amount;

    public RandomOrisonReward(int amount) {
        super(ICON, TEXT, TYPE);
        this.amount = amount;
        cardsToApplyOrison = getCardsWithoutOrisonFirst(
                AbstractDungeon.player.masterDeck.group, amount, AbstractDungeon.cardRandomRng);
        orisons = OrisonLib.getRandomCommonOrison(false, cardsToApplyOrison.size(), false);
        if (orisons.size() < cardsToApplyOrison.size())
            orisons.addAll(OrisonLib.getRandomCommonOrison(false, cardsToApplyOrison.size() - orisons.size(), true));
        initializeCardsToDisplay();
    }
}
