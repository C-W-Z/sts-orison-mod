package orison.core.rewards;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;

import basemod.abstracts.CustomReward;
import basemod.helpers.CardModifierManager;
import orison.core.abstracts.AbstractOrison;

public abstract class AbstractOrisonReward extends CustomReward {

    protected String text;
    protected List<AbstractOrison> orisons;
    protected List<AbstractCard> cardsToApplyOrison;

    public AbstractOrisonReward(Texture icon, String text, RewardType type) {
        super(icon, text, type);
        this.text = text;
    }

    public boolean canAddToRewards() {
        return !orisons.isEmpty() && orisons.size() == cardsToApplyOrison.size();
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(cards, text, true);
            AbstractDungeon.cardRewardScreen.rItem = this;
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return false;
    }

    public void takeReward() {
        int i = cards.indexOf(AbstractDungeon.cardRewardScreen.discoveryCard);
        if (i < 0) {
            // TODO: log error
            return;
        }
        CardModifierManager.addModifier(cardsToApplyOrison.get(i), orisons.get(i));
    }

    public void initializeCardsToDisplay() {
        cards = new ArrayList<>();
        for (int i = 0; i < orisons.size() && i < cardsToApplyOrison.size(); i++) {
            AbstractCard tmpCard = cardsToApplyOrison.get(i).makeStatEquivalentCopy();
            CardModifierManager.addModifier(tmpCard, orisons.get(i));
            cards.add(tmpCard);
        }
    }

    public static List<AbstractCard> getCardsWithoutOrison(List<AbstractCard> cards, int amount, Random random) {
        List<AbstractCard> orisonCards = new ArrayList<>();
        List<AbstractCard> noOrisonCards = new ArrayList<>();
        for (AbstractCard c : cards) {
            if (CardModifierManager.modifiers(c).stream().anyMatch(AbstractOrison.class::isInstance))
                orisonCards.add(c);
            else
                noOrisonCards.add(c);
        }
        List<AbstractCard> ret = new ArrayList<>();
        while (ret.size() < amount && !noOrisonCards.isEmpty()) {
            AbstractCard c = noOrisonCards.get(random.random(noOrisonCards.size() - 1));
            ret.add(c);
            noOrisonCards.remove(c);
        }
        while (ret.size() < amount && !orisonCards.isEmpty()) {
            AbstractCard c = orisonCards.get(random.random(orisonCards.size() - 1));
            ret.add(c);
            orisonCards.remove(c);
        }
        return ret;
    }
}
