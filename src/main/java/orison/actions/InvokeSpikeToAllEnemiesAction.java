package orison.actions;

import static orison.utils.Wiz.getLogicalPowerAmount;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.ThornsPower;

import orison.core.patches.fix.DamageAllEnemiesActionPatch;

/** @see {@link DamageAllEnemiesActionPatch} */
public class InvokeSpikeToAllEnemiesAction extends AbstractGameAction {

    public InvokeSpikeToAllEnemiesAction(AbstractPlayer source, int percent) {
        this.source = source;
        this.amount = percent;
        this.actionType = ActionType.DAMAGE;
        this.damageType = DamageType.THORNS;
        this.attackEffect = AttackEffect.SLASH_HORIZONTAL;
    }

    @Override
    public void update() {
        isDone = true;
        int thorns = getLogicalPowerAmount(source, ThornsPower.POWER_ID);
        int damage = MathUtils.round(thorns * amount / 100F);
        addToTop(new DamageAllEnemiesAction((AbstractPlayer) source, damage, damageType, attackEffect));
    }
}
