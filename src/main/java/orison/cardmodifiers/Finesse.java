package orison.cardmodifiers;

import static orison.OrisonMod.makeID;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.abstracts.AbstractCardModifier.SaveIgnore;

// 妙手
@SaveIgnore
public class Finesse extends AbstractOrison {

    public static final String ID = makeID(Finesse.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Finesse() {
        this(false);
    }

    public Finesse(boolean adv) {
        super(ID, true, false, adv);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        addToBot(new DrawCardAction(adv ? 2 : 1));
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Finesse(adv);
    }

    @Override
    public String getTitle() {
        return uiStrings.TEXT[0];
    }

    @Override
    public String getDescription() {
        return String.format(uiStrings.TEXT[1], adv ? 2 : 1);
    }
}
