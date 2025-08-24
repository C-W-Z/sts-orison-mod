package orison.utils;

import java.util.ArrayList;
import java.util.List;

import com.megacrit.cardcrawl.cards.AbstractCard;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import orison.core.abstracts.AbstractOrison;

public class OrisonHelper {

    public static boolean hasOrisons(AbstractCard card) {
        for (AbstractCardModifier m : CardModifierManager.modifiers(card))
            if (m instanceof AbstractOrison)
                return true;
        return false;
    }

    public static List<AbstractOrison> getOrisons(AbstractCard card) {
        List<AbstractOrison> list = new ArrayList<>();
        for (AbstractCardModifier m : CardModifierManager.modifiers(card))
            if (m instanceof AbstractOrison)
                list.add((AbstractOrison) m);
        return list;
    }
}
