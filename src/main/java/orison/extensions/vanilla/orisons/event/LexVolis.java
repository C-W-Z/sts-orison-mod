package orison.extensions.vanilla.orisons.event;

import java.util.Arrays;
import java.util.List;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import basemod.helpers.CardModifierManager;
import static orison.core.OrisonMod.makeID;

import orison.cardmodifiers.ChangeCostUntilUseModifier;
import orison.core.abstracts.AbstractOrison;
import static orison.utils.Wiz.actB;

// 歡愉
@SaveIgnore
public class LexVolis extends AbstractOrison {

    public static final String ID = makeID(LexVolis.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public LexVolis() {
        this(false);
    }

    public LexVolis(boolean adv) {
        super(ID, true, false, adv);
        // eventID = ;
    }

    @Override
    protected void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        actB(() -> {
            int handSize = AbstractDungeon.player.hand.size();
            List<AbstractCard> allCards = CardLibrary.getAllCards();
            for (int i = 0; i < handSize; i++) {
                int index = AbstractDungeon.cardRandomRng.random(allCards.size() - 1);
                AbstractCard copy = allCards.get(index).makeCopy();
                CardModifierManager.addModifier(copy, new ChangeCostUntilUseModifier(-getModifiedValue(0)));
                addToTop(new MakeTempCardInHandAction(copy, 1));
                allCards.remove(index);
            }
            addToTop(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, handSize, true));
        });
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new LexVolis(adv);
    }

    @Override
    public String getTitle() {
        return uiStrings.TEXT[adv ? 1 : 0];
    }

    @Override
    public String getDescription() {
        if (getModifiedValue(0) <= 0)
            return String.format(uiStrings.TEXT[2]);
        return String.format(uiStrings.TEXT[3], getModifiedValue(0));
    }

    /* ========== Configs ========== */

    protected static List<Integer> values = Arrays.asList(0);
    protected static List<Integer> advValues = Arrays.asList(1);

    @Override
    protected List<Integer> getValueList() {
        return adv ? advValues : values;
    }

    @Override
    protected float getDefaultRarity() {
        return 0;
    }

    @Override
    public UseType getDefaultUseType() {
        return UseType.FINITE_BATTLE;
    }

    @Override
    public boolean canSetRarity() {
        return false;
    }
}
