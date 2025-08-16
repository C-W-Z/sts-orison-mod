package orison.core.cardmodifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.InnateMod;
import basemod.helpers.CardModifierManager;

import static orison.core.OrisonMod.makeID;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.CommonKeywordIconsField;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class InnateModifier extends AbstractCardModifier {

    public static final String ID = makeID(InnateModifier.class.getSimpleName());

    public InnateModifier() {}

    public boolean shouldApply(AbstractCard card) {
        if (card.isInnate)
            return false;
        if (CommonKeywordIconsField.useIcons.get(card))
            return true;
        CardModifierManager.addModifier(card, new InnateMod());
        return false;
    }

    public void onInitialApplication(AbstractCard card) {
        card.isInnate = true;
    }

    public void onRemove(AbstractCard card) {
        card.isInnate = false;
    }

    public AbstractCardModifier makeCopy() {
        return new InnateModifier();
    }

    public String identifier(AbstractCard card) {
        return ID;
    }
}
