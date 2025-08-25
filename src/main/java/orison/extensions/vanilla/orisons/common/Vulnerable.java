package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.core.abstracts.AbstractOrison;

// 易傷
@SaveIgnore
public class Vulnerable extends AbstractOrison {

    public static final String ID = makeID(Vulnerable.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Vulnerable() {
        this(false);
        values.add(1);
        advValues.add(2);
    }

    public Vulnerable(boolean adv) {
        super(ID, true, false, adv);
    }

    @Override
    protected void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        int val = getModifiedValue(0);
        addToBot(new AllEnemyApplyPowerAction(AbstractDungeon.player, val,
                m -> new VulnerablePower(m, val, false)));
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Vulnerable(adv);
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
