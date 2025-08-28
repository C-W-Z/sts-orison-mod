package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import java.util.Arrays;
import java.util.List;

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

    @Override
    protected void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new ThornsPower(AbstractDungeon.player, getModifiedValue(0))));
        addToBot(new InvokeSpikeToAllEnemiesAction(AbstractDungeon.player, getModifiedValue(1)));
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
        return String.format(uiStrings.TEXT[2], getModifiedValue(0), getModifiedValue(1));
    }

    /* ========== Configs ========== */

    protected static List<Integer> values = Arrays.asList(3, 25);
    protected static List<Integer> advValues = Arrays.asList(6, 50);

    @Override
    protected List<Integer> getValueList() {
        return adv ? advValues : values;
    }
}
