package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import java.util.List;

import com.megacrit.cardcrawl.cards.AbstractCard;

import basemod.helpers.CardModifierManager;
import orison.core.abstracts.AbstractOrison;
import orison.core.abstracts.AbstractOrisonRelic;
import orison.core.libs.OrisonLib;
import orison.core.patches.InitializeDeckPatch;
import orison.core.patches.OnMakeCardInCombatPatch;
import orison.utils.OrisonHelper;

/** @see OnMakeCardInCombatPatch */
public class OrganicForm extends AbstractOrisonRelic {

    public static final String ID = makeID(OrganicForm.class.getSimpleName());

    public OrganicForm() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    /** @see InitializeDeckPatch */
    public static void onCardMakeInBattle(AbstractCard c) {
        if (!AbstractOrison.canAttachOrison(c) || OrisonHelper.hasOrisons(c))
            return;
        List<AbstractOrison> orison = OrisonLib.getRandomCommonOrison(false, 1, false);
        if (orison.isEmpty())
            return;
        CardModifierManager.addModifier(c, orison.get(0));
        InitializeDeckPatch.onDeckInit(c);
    }
}
