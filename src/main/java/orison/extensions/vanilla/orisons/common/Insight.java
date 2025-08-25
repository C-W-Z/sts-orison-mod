package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.core.abstracts.AbstractOrison;

// 靈感
@SaveIgnore
public class Insight extends AbstractOrison {

    public static final String ID = makeID(Insight.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    private static AbstractCard cardToShuffle = new com.megacrit.cardcrawl.cards.tempCards.Insight();

    public Insight() {
        this(false);
    }

    public Insight(boolean adv) {
        super(ID, DEFAULT_RARITY / 2F, true, false, adv);
        values.add(1);
        advValues.add(2);
    }

    @Override
    protected void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        // 裡面會呼叫makeStatEquivalentCopy()
        addToBot(new MakeTempCardInDrawPileAction(cardToShuffle, getModifiedValue(0), true, true));
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Insight(adv);
    }

    @Override
    public String getTitle() {
        return uiStrings.TEXT[adv ? 1 : 0];
    }

    @Override
    public String getDescription() {
        return String.format(uiStrings.TEXT[2], getModifiedValue(0), cardToShuffle.name);
    }
}
