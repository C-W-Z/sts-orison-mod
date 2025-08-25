package orison.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class AllEnemiesLoseStrengthAction extends AbstractGameAction {
    private ArrayList<ApplyPowerAction> actions = new ArrayList<>();

    public AllEnemiesLoseStrengthAction(AbstractCreature source, int amount) {
        this.source = source;
        this.amount = amount;
        this.actionType = ActionType.POWER;
        AbstractDungeon.getMonsters().monsters.stream().filter((m) -> {
            return !m.isDeadOrEscaped();
        }).forEach((q) -> {
            this.actions.add(new ApplyPowerAction(q, source, new StrengthPower(q, -amount)));
            if (!q.hasPower(ArtifactPower.POWER_ID))
                this.actions.add(new ApplyPowerAction(q, source, new GainStrengthPower(q, amount)));
        });
    }

    public void update() {
        this.isDone = true;
        if (!(AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
            for (AbstractGameAction action : this.actions) {
                if (!action.isDone) {
                    action.update();
                    this.isDone = false;
                }
            }
    }
}
