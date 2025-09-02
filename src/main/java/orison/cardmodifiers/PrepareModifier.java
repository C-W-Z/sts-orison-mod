package orison.cardmodifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import orison.core.patches.PreparePatch;

import static orison.core.OrisonMod.makeID;

import java.util.List;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class PrepareModifier extends AbstractCardModifier {

    public static final String ID = makeID(PrepareModifier.class.getSimpleName());
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private int value;

    public PrepareModifier() {
        this(1);
    }

    public PrepareModifier(int value) {
        this.value = value;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return value > 0;
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        List<AbstractCardModifier> ms = CardModifierManager.getModifiers(card, ID);
        if (ms.size() > 1 && ms.get(0) != this)
            return rawDescription;
        return String.format(TEXT[0], rawDescription, PreparePatch.Field.prepare.get(card));
    }

    public void onInitialApplication(AbstractCard card) {
        PreparePatch.Field.prepare.set(card, Math.max(0, PreparePatch.Field.prepare.get(card) + value));
    }

    public void onRemove(AbstractCard card) {
        PreparePatch.Field.prepare.set(card, Math.max(0, PreparePatch.Field.prepare.get(card) - value));
    }

    public AbstractCardModifier makeCopy() {
        return new PrepareModifier(value);
    }

    public String identifier(AbstractCard card) {
        return ID;
    }
}
