package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.PoisonPower;

import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.actions.TriggerAllEnemiesPoisonAction;
import orison.core.abstracts.AbstractOrison;

// 中毒
@SaveIgnore
public class Toxin extends AbstractOrison {

    public static final String ID = makeID(Toxin.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Toxin() {
        this(false);
    }

    public Toxin(boolean adv) {
        super(ID, true, false, adv);
    }

    public static int getValue(boolean adv) {
        return adv ? 6 : 3;
    }

    public static int getValue2(boolean adv) {
        return adv ? 30 : 15;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        addToBot(new AllEnemyApplyPowerAction(AbstractDungeon.player, getValue(adv),
                m -> new PoisonPower(m, AbstractDungeon.player, getValue(adv))));
        addToBot(new TriggerAllEnemiesPoisonAction(AbstractDungeon.player, getValue2(adv)));
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Toxin(adv);
    }

    @Override
    public String getTitle() {
        return uiStrings.TEXT[adv ? 1 : 0];
    }

    @Override
    public String getDescription() {
        return String.format(uiStrings.TEXT[2], getValue(adv), getValue2(adv));
    }
}
