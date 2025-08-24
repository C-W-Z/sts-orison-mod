package orison.core.abstracts;

import static orison.core.OrisonMod.makeID;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import basemod.Pair;
import basemod.abstracts.CustomReward;
import basemod.helpers.CardModifierManager;
import orison.cardmodifiers.ShowOrisonChangeModifier;
import orison.utils.OrisonHelper;

public abstract class AbstractOrisonReward extends CustomReward {

    private static final Logger logger = LogManager.getLogger(AbstractOrison.class);
    public static final String[] TEXT = CardCrawlGame.languagePack
            .getUIString(makeID(AbstractOrisonReward.class.getSimpleName())).TEXT;

    protected List<AbstractOrison> orisons;
    protected List<AbstractCard> cardsToApplyOrison;
    protected ArrayList<AbstractCard> cardsToDisplay;

    public AbstractOrisonReward(Texture icon, RewardType type) {
        super(icon, TEXT[0], type);
    }

    public boolean canAddToRewards() {
        return !orisons.isEmpty() && orisons.size() == cardsToApplyOrison.size();
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(cardsToDisplay, TEXT[0], true);
            AbstractDungeon.cardRewardScreen.rItem = this;
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return false;
    }

    public void takeReward() {
        int i = cardsToDisplay.indexOf(AbstractDungeon.cardRewardScreen.discoveryCard);
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
        cardsToDisplay = new ArrayList<>();
        for (int i = 0; i < orisons.size() && i < cardsToApplyOrison.size(); i++) {
            AbstractCard tmpCard = cardsToApplyOrison.get(i).makeStatEquivalentCopy();
            List<AbstractOrison> oldOrison = OrisonHelper.getOrisons(tmpCard);
            if (oldOrison.isEmpty())
                CardModifierManager.addModifier(tmpCard, orisons.get(i));
            else
                CardModifierManager.addModifier(tmpCard, new ShowOrisonChangeModifier(oldOrison.get(0), orisons.get(i)));
            cardsToDisplay.add(tmpCard);
        }
    }

    /** @return Pair of no-orison card list and with-orison card list */
    protected static Pair<List<AbstractCard>, List<AbstractCard>> getCardsWithoutOrisonFirst(
            List<AbstractCard> cards, int amount, Random random) {
        List<AbstractCard> orisonCards = new ArrayList<>();
        List<AbstractCard> noOrisonCards = new ArrayList<>();
        for (AbstractCard c : cards) {
            if (!AbstractOrison.canApplyOrison(c))
                continue;
            if (OrisonHelper.hasOrisons(c))
                orisonCards.add(c);
            else
                noOrisonCards.add(c);
        }
        List<AbstractCard> first = new ArrayList<>();
        while (first.size() < amount && !noOrisonCards.isEmpty()) {
            AbstractCard c = noOrisonCards.get(random.random(noOrisonCards.size() - 1));
            first.add(c);
            noOrisonCards.remove(c);
            logger.info("Rolled no-orison card: " + c.cardID);
        }
        List<AbstractCard> second = new ArrayList<>();
        while (first.size() + second.size() < amount && !orisonCards.isEmpty()) {
            AbstractCard c = orisonCards.get(random.random(orisonCards.size() - 1));
            second.add(c);
            orisonCards.remove(c);
            logger.info("Rolled with-orison card: " + c.cardID);
        }
        return new Pair<>(first, second);
    }
}
