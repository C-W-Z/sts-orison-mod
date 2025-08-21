package orison.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MultiGroupSelectAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public class ForgingAction extends MultiGroupSelectAction {
    public ForgingAction(int amount) {
        super("Select " + amount + " card" + (amount > 1 ? "s" : "") + " to Upgrade", (cards, card2Group) -> {
            for (AbstractCard c : cards) {
                c.upgrade();
                AbstractCard tmpCard = c.makeStatEquivalentCopy();
                AbstractDungeon.effectList.add(0, new ShowCardBrieflyEffect(tmpCard));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(tmpCard.target_x, tmpCard.target_y));
            }
        }, amount, c -> c.canUpgrade(), CardGroupType.DRAW_PILE, CardGroupType.DISCARD_PILE);
    }
}
