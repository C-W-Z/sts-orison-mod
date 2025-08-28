package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import java.util.Arrays;
import java.util.List;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.core.abstracts.AbstractOrison;

// 籌算
@SaveIgnore
public class Computation extends AbstractOrison {

    public static final String ID = makeID(Computation.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Computation() {
        this(false);
    }

    public Computation(boolean adv) {
        super(ID, true, false, adv);
    }

    @Override
    protected void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        addToBot(new GainEnergyAction(getModifiedValue(0)));
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Computation(adv);
    }

    @Override
    public String getTitle() {
        return uiStrings.TEXT[adv ? 1 : 0];
    }

    @Override
    public String getDescription() {
        return String.format(uiStrings.TEXT[2], getModifiedValue(0));
    }

    /* ========== Configs ========== */

    protected static List<Integer> values = Arrays.asList(1);
    protected static List<Integer> advValues = Arrays.asList(2);

    @Override
    protected List<Integer> getValueList() {
        return adv ? advValues : values;
    }

    @Override
    protected float getDefaultRarity() {
        return super.getDefaultRarity() / 2F;
    }
}
