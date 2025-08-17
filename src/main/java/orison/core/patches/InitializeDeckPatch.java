package orison.core.patches;

import java.util.ArrayList;
import java.util.List;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import javassist.CtBehavior;
import orison.core.interfaces.OnInitializeDeck;

@SpirePatch2(clz = CardGroup.class, method = "initializeDeck")
public class InitializeDeckPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = { "copy" })
    public static void Insert(CardGroup __instance, CardGroup masterDeck, CardGroup copy) {
        for (AbstractCard card : copy.group) {

            List<AbstractCardModifier> modsToAdd = new ArrayList<>();

            for (AbstractCardModifier m : CardModifierManager.modifiers(card))
                if (m instanceof OnInitializeDeck) {
                    List<AbstractCardModifier> mods = ((OnInitializeDeck) m).onInitDeckToAddModifiers();
                    if (mods != null)
                        modsToAdd.addAll(mods);
                }

            for (AbstractCardModifier m : modsToAdd)
                CardModifierManager.addModifier(card, m);
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(CardGroup.class, "shuffle");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}
