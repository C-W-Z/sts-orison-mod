package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import java.util.Arrays;
import java.util.List;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import basemod.helpers.CardModifierManager;
import orison.cardmodifiers.RetainModifier;
import orison.core.abstracts.AbstractOrison;
import orison.core.interfaces.OnInitializeDeck;

// 鏡像
@SaveIgnore
public class Mirror extends AbstractOrison implements OnInitializeDeck {

    public static final String ID = makeID(Mirror.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Mirror() {
        this(false);
    }

    public Mirror(boolean adv) {
        super(ID, true, false, adv);
    }

    @Override
    public List<AbstractCardModifier> onInitDeckToAddModifiers() {
        return Arrays.asList(new RetainModifier());
    }

    @Override
    public boolean onBattleStart(AbstractCard card) {
        super.onBattleStart(card);
        AbstractCard copy = card.makeStatEquivalentCopy();
        CardModifierManager.removeModifiersById(copy, id, true);
        addToBot(new MakeTempCardInDrawPileAction(copy, getModifiedValue(0), true, true));
        return true;
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Mirror(adv);
    }

    @Override
    public String getTitle() {
        return uiStrings.TEXT[adv ? 1 : 0];
    }

    @Override
    public String getDescription() {
        return String.format(uiStrings.TEXT[2], getModifiedValue(0));
    }

    @Override
    protected void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action) {}

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
