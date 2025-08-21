package orison.extensions.vanilla.orisons.common;

import static orison.core.OrisonMod.makeID;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.core.abstracts.AbstractOrison;

// 治療
@SaveIgnore
public class Healing extends AbstractOrison {

    public static final String ID = makeID(Healing.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public Healing() {
        this(false);
    }

    public Healing(boolean adv) {
        super(ID, DEFAULT_RARITY / 2F, true, false, adv);
    }

    public static int getValue(boolean adv) {
        return adv ? 2 : 1;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, getValue(adv)));
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new Healing(adv);
    }

    @Override
    public String getTitle() {
        return uiStrings.TEXT[adv ? 1 : 0];
    }

    @Override
    public String getDescription() {
        return String.format(uiStrings.TEXT[2], getValue(adv));
    }
}
