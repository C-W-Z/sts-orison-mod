package orison.core.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import orison.core.interfaces.AtStartOfTurnModifier;

@SpirePatch2(clz = AbstractPlayer.class, method = "applyStartOfTurnCards")
public class AtStartOfTurnModifierPatch {
    @SpirePostfixPatch
    public static void Postfix(AbstractPlayer __instance) {
        for (AbstractCard c : __instance.drawPile.group) {
            if (c == null)
                continue;
            for (AbstractCardModifier m : CardModifierManager.modifiers(c))
                if (m instanceof AtStartOfTurnModifier)
                    ((AtStartOfTurnModifier) m).atStartOfTurn(c, __instance.drawPile);
        }
        for (AbstractCard c : __instance.hand.group) {
            if (c == null)
                continue;
            for (AbstractCardModifier m : CardModifierManager.modifiers(c))
                if (m instanceof AtStartOfTurnModifier)
                    ((AtStartOfTurnModifier) m).atStartOfTurn(c, __instance.hand);
        }
        for (AbstractCard c : __instance.discardPile.group) {
            if (c == null)
                continue;
            for (AbstractCardModifier m : CardModifierManager.modifiers(c))
                if (m instanceof AtStartOfTurnModifier)
                    ((AtStartOfTurnModifier) m).atStartOfTurn(c, __instance.discardPile);
        }
    }
}
