package orison.cardmodifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.AbstractCardModifier.SaveIgnore;

import static orison.core.OrisonMod.makeID;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;

@SaveIgnore
public class ChangeCostUntilUseModifier extends AbstractCardModifier {

    public static final String ID = makeID(ChangeCostUntilUseModifier.class.getSimpleName());

    private int amount;

    public ChangeCostUntilUseModifier(int amount) {
        this.amount = amount;
    }

    public boolean shouldApply(AbstractCard card) {
        return card.cost >= 0 && amount != 0;
    }

    public void onInitialApplication(AbstractCard card) {
        if (card.cost <= 0)
        {
            amount = 0;
            return;
        }
        if (amount < 0)
            amount = Math.max(-card.cost, amount);
        card.updateCost(amount);
    }

    public void onRemove(AbstractCard card) {
        if (amount == 0)
            return;
        card.updateCost(-amount);
        card.isCostModifiedForTurn = (card.costForTurn != card.cost);
        AbstractCard tmp = CardLibrary.getCopy(card.cardID, card.timesUpgraded, card.misc);
        card.isCostModified = (card.cost != tmp.cost);
    }

    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }

    public AbstractCardModifier makeCopy() {
        return new ChangeCostUntilUseModifier(amount);
    }

    public String identifier(AbstractCard card) {
        return ID;
    }
}
