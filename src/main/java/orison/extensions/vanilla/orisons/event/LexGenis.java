package orison.extensions.vanilla.orisons.event;

import java.util.Arrays;
import java.util.List;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import basemod.helpers.CardModifierManager;
import static orison.core.OrisonMod.makeID;

import orison.cardmodifiers.ChangeCostUntilUseModifier;
import orison.cardmodifiers.EtherealModifier;
import orison.cardmodifiers.ExhaustModifier;
import orison.core.abstracts.AbstractOrison;
import static orison.utils.Wiz.actB;

// 繁育
@SaveIgnore
public class LexGenis extends AbstractOrison {

    public static final String ID = makeID(LexGenis.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public LexGenis() {
        this(false);
    }

    public LexGenis(boolean adv) {
        super(ID, true, false, adv);
        // eventID = ;
    }

    @Override
    protected void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractCard copy = card.makeStatEquivalentCopy();
        CardModifierManager.removeAllModifiers(copy, false);
        CardModifierManager.addModifier(copy, new ExhaustModifier());
        CardModifierManager.addModifier(copy, new EtherealModifier());
        CardModifierManager.addModifier(copy, new ChangeCostUntilUseModifier(-getModifiedValue(0)));
        actB(() -> {
            addToTop(new MakeTempCardInHandAction(copy, BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.hand.size()));
        });
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new LexGenis(adv);
    }

    @Override
    public String getTitle() {
        return uiStrings.TEXT[adv ? 1 : 0];
    }

    @Override
    public String getDescription() {
        if (getModifiedValue(0) <= 0)
            return String.format(uiStrings.TEXT[2]);
        return String.format(uiStrings.TEXT[3], getModifiedValue(0));
    }

    /* ========== Configs ========== */

    protected static List<Integer> values = Arrays.asList(0);
    protected static List<Integer> advValues = Arrays.asList(1);

    @Override
    public List<Integer> getValueList() {
        return adv ? advValues : values;
    }

    @Override
    protected float getDefaultRarity() {
        return 0;
    }

    @Override
    public UseType getDefaultUseType() {
        return UseType.FINITE_BATTLE;
    }

    @Override
    public boolean canSetRarity() {
        return false;
    }
}
