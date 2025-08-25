package orison.core.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

public interface AtStartOfTurnModifier {
    void atStartOfTurn(AbstractCard card, CardGroup group);
}
