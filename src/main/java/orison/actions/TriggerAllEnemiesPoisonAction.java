package orison.actions;

import java.util.ArrayList;
import java.util.List;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TriggerAllEnemiesPoisonAction extends AbstractGameAction {
    private List<TriggerPoisonAction> actions = new ArrayList<>();

    public TriggerAllEnemiesPoisonAction(AbstractCreature source, int percent) {
        this.source = source;
        this.amount = percent;
        this.actionType = ActionType.DAMAGE;
        AbstractDungeon.getMonsters().monsters.stream().filter((m) -> {
            return !m.isDeadOrEscaped();
        }).forEach((q) -> {
            this.actions.add(new TriggerPoisonAction(q, this.source, percent));
        });
    }

    public void update() {
        this.isDone = true;
        if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
            return;
        for (AbstractGameAction action : this.actions) {
            if (!action.isDone) {
                action.update();
                this.isDone = false;
            }
        }
    }
}
