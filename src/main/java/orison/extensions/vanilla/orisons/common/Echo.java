package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import java.util.Arrays;
import java.util.List;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.cardmodifiers.ExhaustModifier;
import orison.cardmodifiers.InnateModifier;
import orison.core.abstracts.AbstractOrison;
import orison.core.interfaces.OnInitializeDeck;

// 回聲
@SaveIgnore
public class Echo extends AbstractOrison implements OnInitializeDeck {

    public static final String ID = makeID(Echo.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Echo() {
        this(false);
    }

    public Echo(boolean adv) {
        super(ID, DEFAULT_RARITY / 2F, true, false, adv);
    }

    public static int getValue(boolean adv) {
        return adv ? 2 : 1;
    }

    public static int getValue2(boolean adv) {
        return adv ? 10 : 5;
    }

    @Override
    public List<AbstractCardModifier> onInitDeckToAddModifiers() {
        return Arrays.asList(new InnateModifier(), new ExhaustModifier());
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (card.purgeOnUse)
            return;
        AbstractMonster m = null;
        if (action.target instanceof AbstractMonster)
            m = (AbstractMonster) action.target;
        for (int i = 0; i < getValue(adv); i++)
            GameActionManager.queueExtraCard(card, m);
        addToBot(new GainGoldAction(getValue2(adv)));
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Echo(adv);
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
