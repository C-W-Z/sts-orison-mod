package orison.cardmodifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;

import static orison.core.OrisonMod.makeID;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.CommonKeywordIconsField;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class RetainModifier extends AbstractCardModifier {

    public static final String ID = makeID(RetainModifier.class.getSimpleName());

    public RetainModifier() {}

    public boolean shouldApply(AbstractCard card) {
        if (card.selfRetain)
            return false;
        if (CommonKeywordIconsField.useIcons.get(card))
            return true;
        CardModifierManager.addModifier(card, new RetainMod());
        return false;
    }

    public void onInitialApplication(AbstractCard card) {
        card.selfRetain = true;
    }

    public void onRemove(AbstractCard card) {
        card.selfRetain = false;
    }

    public AbstractCardModifier makeCopy() {
        return new RetainModifier();
    }

    public String identifier(AbstractCard card) {
        return ID;
    }
}
