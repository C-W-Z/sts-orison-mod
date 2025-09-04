package orison.extensions.vanilla.orisons.event;

import static orison.core.OrisonMod.makeID;

import java.util.Arrays;
import java.util.List;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import basemod.helpers.CardModifierManager;
import orison.cardmodifiers.ExhaustModifier;
import orison.cardmodifiers.PrepareModifier;
import orison.cardmodifiers.RetainModifier;
import orison.core.abstracts.AbstractOrison;
import orison.core.interfaces.OnInitializeDeck;

// 孕育
@SaveIgnore
public class Birth extends AbstractOrison implements OnInitializeDeck {

    public static final String ID = makeID(Birth.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Birth() {
        this(false);
    }

    public Birth(boolean adv) {
        super(ID, true, false, adv);
        priority = -1;
    }

    @Override
    public List<AbstractCardModifier> onInitDeckToAddModifiers() {
        return Arrays.asList(new RetainModifier(), new PrepareModifier(getModifiedValue(1)));
    }

    @Override
    protected void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    }

    @Override
    public void atEndOfTurn(AbstractCard card, CardGroup group) {
        if (group.type != CardGroupType.HAND)
            return;
        AbstractCard card2 = card.makeStatEquivalentCopy();
        CardModifierManager.removeAllModifiers(card2, false);
        CardModifierManager.addModifier(card2, new ExhaustModifier());
        CardModifierManager.addModifier(card2, new RetainModifier());
        CardModifierManager.addModifier(card2, new PrepareModifier(getModifiedValue(1)));
        addToBot(new MakeTempCardInHandAction(card2, getModifiedValue(0)));
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Birth(adv);
    }

    @Override
    public String getTitle() {
        return uiStrings.TEXT[adv ? 1 : 0];
    }

    @Override
    public String getDescription() {
        int prepare = getModifiedValue(1);
        if (prepare <= 0)
            return String.format(uiStrings.TEXT[2], getModifiedValue(0));
        return String.format(uiStrings.TEXT[3], prepare, getModifiedValue(0), prepare);
    }

    /* ========== Configs ========== */

    protected static List<Integer> values = Arrays.asList(1, 0);
    protected static List<Integer> advValues = Arrays.asList(1, 1);

    @Override
    protected List<Integer> getValueList() {
        return adv ? advValues : values;
    }

    @Override
    protected float getDefaultRarity() {
        return 0;
    }

    @Override
    public UseType getUseType() {
        return UseType.INFINITE;
    }

    @Override
    public boolean canSetRarity() {
        return false;
    }

    @Override
    public boolean canSetUseType() {
        return false;
    }

    @Override
    public boolean canSetUseLimit() {
        return false;
    }
}
