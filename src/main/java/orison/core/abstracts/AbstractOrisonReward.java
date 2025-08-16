package orison.core.abstracts;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.CustomReward;
import basemod.helpers.CardModifierManager;
import orison.core.cardmodifiers.ShowOrisonChangeModifier;

public abstract class AbstractOrisonReward extends CustomReward {

    private static final Logger logger = LogManager.getLogger(AbstractOrison.class);

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
            logger.error("discoveryCard NOT found in Orison Reward cards");
            return;
        }
        CardModifierManager.addModifier(cardsToApplyOrison.get(i), orisons.get(i));
        // 不知道為啥在AbstractDungeon.effectList加ShowCardBrieflyEffect沒有任何反應，要加在topLevelEffects
        // 明明那些升級卡牌的事件是加在effectList的，很奇怪
        // 也有可能是effectList會被CombatRewardScreen擋住？
        AbstractDungeon.topLevelEffects.add(
                new ShowCardBrieflyEffect(cardsToApplyOrison.get(i).makeStatEquivalentCopy(),
                        Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
    }

    /** Call this function at the end of OrisonRewards' Contructor */
    protected void initializeCardsToDisplay() {
        cards = new ArrayList<>();
        for (int i = 0; i < orisons.size() && i < cardsToApplyOrison.size(); i++) {
            AbstractCard tmpCard = cardsToApplyOrison.get(i).makeStatEquivalentCopy();
            AbstractOrison oldOrison = getOldOrison(tmpCard);
            if (oldOrison == null)
                CardModifierManager.addModifier(tmpCard, orisons.get(i));
            else
                CardModifierManager.addModifier(tmpCard, new ShowOrisonChangeModifier(oldOrison, orisons.get(i)));
            cards.add(tmpCard);
        }
    }

    protected static List<AbstractCard> getCardsWithoutOrisonFirst(List<AbstractCard> cards, int amount,
            Random random) {
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

    protected static AbstractOrison getOldOrison(AbstractCard card) {
        for (AbstractCardModifier m : CardModifierManager.modifiers(card))
            if (m instanceof AbstractOrison)
                return (AbstractOrison) m;
        return null;
    }
}
