package orison.actions;

import static orison.utils.Wiz.getLogicalPowerAmount;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class TriggerPoisonAction extends AbstractGameAction {

    private static final float DURATION = 0.33F;

    public TriggerPoisonAction(AbstractCreature target, AbstractCreature source, int percent) {
        setValues(target, source, percent);
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.attackEffect = AttackEffect.POISON;
        this.duration = DURATION;
    }

    public void update() {
        if ((AbstractDungeon.getCurrRoom()).phase != AbstractRoom.RoomPhase.COMBAT) {
            this.isDone = true;
            return;
        }

        if (this.duration == DURATION && this.target.currentHealth > 0) {
            if (getLogicalPowerAmount(this.target, PoisonPower.POWER_ID) == 0) {
                this.isDone = true;
                return;
            }

            AbstractDungeon.effectList.add(
                    new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
        }

        tickDuration();

        if (!this.isDone)
            return;

        if (this.target.currentHealth > 0) {
            this.target.tint.color = Color.CHARTREUSE.cpy();
            this.target.tint.changeColor(Color.WHITE.cpy());

            int damage = MathUtils.round(getLogicalPowerAmount(this.target, PoisonPower.POWER_ID) * amount / 100F);
            if (damage <= 0)
                return;

            this.target.damage(new DamageInfo(this.source, damage, DamageInfo.DamageType.HP_LOSS));
            if (this.target.isDying && this.source.isPlayer) {
                AbstractPlayer.poisonKillCount++;
                if (AbstractPlayer.poisonKillCount == 3
                        && AbstractDungeon.player.chosenClass == AbstractPlayer.PlayerClass.THE_SILENT)
                    UnlockTracker.unlockAchievement("PLAGUE");
            }
        }

        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
            AbstractDungeon.actionManager.clearPostCombatActions();
        addToTop((AbstractGameAction) new WaitAction(0.1F));
    }
}
