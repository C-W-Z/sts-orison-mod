package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.WeakPower;

import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.core.abstracts.AbstractOrison;

// 虛弱
@SaveIgnore
public class Weakness extends AbstractOrison {

    public static final String ID = makeID(Weakness.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Weakness() {
        this(false);
    }

    public Weakness(boolean adv) {
        super(ID, true, false, adv);
        values.add(1);
        advValues.add(2);
    }

    @Override
    protected void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        int val = getModifiedValue(0);
        addToBot(new AllEnemyApplyPowerAction(AbstractDungeon.player, val,
                m -> new WeakPower(m, val, false)));
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Weakness(adv);
    }

    @Override
    public String getTitle() {
        return uiStrings.TEXT[adv ? 1 : 0];
    }

    @Override
    public String getDescription() {
        return String.format(uiStrings.TEXT[2], getModifiedValue(0));
    }
}
