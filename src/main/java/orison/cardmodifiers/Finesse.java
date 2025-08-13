package orison.cardmodifiers;

import static orison.OrisonMod.makeID;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.AbstractCardModifier.SaveIgnore;

@SaveIgnore
public class Finesse extends AbstractOrison {

    public static final String ID = makeID(Finesse.class.getSimpleName());

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
    public AbstractCardModifier makeCopy() {
        return new Finesse(adv);
    }
}
