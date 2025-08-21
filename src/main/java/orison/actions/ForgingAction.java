package orison.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MultiGroupSelectAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;

public class ForgingAction extends MultiGroupSelectAction {
    public ForgingAction(int amount) {
        super("Select " + amount + " card" + (amount > 1 ? "s" : "") + " to Upgrade", (cards, card2Group) -> {
            for (AbstractCard c : cards)
                c.canUpgrade();
        }, amount, c -> c.canUpgrade(), CardGroupType.DRAW_PILE, CardGroupType.DISCARD_PILE);
    }
}
