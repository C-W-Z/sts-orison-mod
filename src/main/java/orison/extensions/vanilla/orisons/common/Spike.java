package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.ThornsPower;

import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.actions.InvokeSpikeToAllEnemiesAction;
import orison.core.abstracts.AbstractOrison;

// 尖刺
@SaveIgnore
public class Spike extends AbstractOrison {

    public static final String ID = makeID(Spike.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Spike() {
        this(false);
    }

    public Spike(boolean adv) {
        super(ID, true, false, adv);
    }

    public static int getValue(boolean adv) {
        return adv ? 6 : 3;
    }

    public static int getValue2(boolean adv) {
        return adv ? 50 : 25;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new ThornsPower(AbstractDungeon.player, getValue(adv))));
        addToBot(new InvokeSpikeToAllEnemiesAction(AbstractDungeon.player, getValue2(adv)));
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Spike(adv);
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
