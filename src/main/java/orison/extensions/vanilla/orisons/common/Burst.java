package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.core.abstracts.AbstractOrison;

// 爆發
@SaveIgnore
public class Burst extends AbstractOrison {

    public static final String ID = makeID(Burst.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Burst() {
        this(false);
    }

    public Burst(boolean adv) {
        super(ID, true, false, adv);
        values.add(2);
        advValues.add(4);
    }

    @Override
    protected void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        int val = getModifiedValue(0);
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new StrengthPower(AbstractDungeon.player, val)));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new LoseStrengthPower(AbstractDungeon.player, val)));
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Burst(adv);
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
