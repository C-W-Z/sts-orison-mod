package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import java.util.Arrays;
import java.util.List;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.actions.ForgingAction;
import orison.core.abstracts.AbstractOrison;

// 鍛造
@SaveIgnore
public class Forging extends AbstractOrison {

    public static final String ID = makeID(Forging.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Forging() {
        this(false);
    }

    public Forging(boolean adv) {
        super(ID, true, false, adv);
    }

    @Override
    protected void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        addToBot(new ForgingAction(getModifiedValue(0)));
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Forging(adv);
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
    public List<Integer> getValueList() {
        return adv ? advValues : values;
    }

    @Override
    protected float getDefaultRarity() {
        return super.getDefaultRarity() / 2F;
    }

    @Override
    public int getValueMaxForConfig(int index) {
        return 20;
    }
}
