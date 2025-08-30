package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import java.util.Arrays;
import java.util.List;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.cardmodifiers.RetainModifier;
import orison.core.abstracts.AbstractOrison;
import orison.core.interfaces.OnInitializeDeck;
import orison.core.patches.PreparePatch;

// 預備
@SaveIgnore
public class Prepare extends AbstractOrison implements OnInitializeDeck {

    public static final String ID = makeID(Prepare.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Prepare() {
        this(false);
    }

    public Prepare(boolean adv) {
        super(ID, true, false, adv);
    }

    @Override
    public List<AbstractCardModifier> onInitDeckToAddModifiers() {
        return Arrays.asList(new RetainModifier());
    }

    @Override
    public boolean onBattleStart(AbstractCard card) {
        super.onBattleStart(card);
        PreparePatch.Field.prepare.set(card, getModifiedValue(0));
        return false;
    }

    @Override
    protected void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action) {}

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Prepare(adv);
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

    @Override
    public UseType getUseType() {
        return UseType.INFINITE;
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
