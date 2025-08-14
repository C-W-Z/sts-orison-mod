package orison.rewards;

import static orison.OrisonMod.makeID;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.abstracts.CustomReward;

public abstract class AbstractOrisonReward extends CustomReward {

    protected static final String TEXT = CardCrawlGame.languagePack
            .getUIString(makeID(AbstractOrisonReward.class.getSimpleName())).TEXT[0];

    protected ArrayList<AbstractCard> wheels;

    public AbstractOrisonReward(Texture icon, RewardType type) {
        super(icon, TEXT, type);
    }

    public boolean isValid() {
        return !wheels.isEmpty();
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            AbstractDungeon.cardRewardScreen.open(wheels, this, TEXT);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return false;
    }
}
