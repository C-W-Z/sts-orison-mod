package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.core.abstracts.AbstractOrison;

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

    public static int getValue(boolean adv) {
        return adv ? 2 : 1;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        addToBot(new DrawCardAction(getValue(adv)));
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Finesse(adv);
    }

    @Override
    public String getTitle() {
        return uiStrings.TEXT[adv ? 1 : 0];
    }

    @Override
    public String getDescription() {
        return String.format(uiStrings.TEXT[2], getValue(adv));
    }
}
