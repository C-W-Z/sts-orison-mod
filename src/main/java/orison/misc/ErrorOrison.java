package orison.misc;

import static orison.core.OrisonMod.makeID;

import java.util.ArrayList;
import java.util.List;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

import basemod.AutoAdd;
import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import orison.core.abstracts.AbstractOrison;

@AutoAdd.Ignore
@SaveIgnore
public class ErrorOrison extends AbstractOrison {

    public static final String ID = makeID(ErrorOrison.class.getSimpleName());

    public ErrorOrison() {
        this(false);
    }

    public ErrorOrison(boolean adv) {
        super(ID, false, false, false);
    }

    @Override
    public AbstractOrison newInstance(boolean adv) {
        return new ErrorOrison(adv);
    }

    @Override
    public String getTitle() {
        return "Error";
    }

    @Override
    public String getDescription() {
        return "Something wrong to load the Orison.";
    }

    @Override
    protected void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action) {}

    @Override
    protected List<Integer> getValueList() {
        return new ArrayList<>();
    }

    @Override
    public float getRarity() {
        return 0;
    }

    @Override
    public UseType getUseType() {
        return UseType.INFINITE;
    }

    @Override
    public int getMaxUses() {
        return -1;
    }
}
