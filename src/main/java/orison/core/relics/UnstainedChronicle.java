package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import java.util.List;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;

import basemod.helpers.CardModifierManager;
import orison.core.abstracts.AbstractOrison;
import orison.core.abstracts.AbstractOrisonRelic;
import orison.core.libs.OrisonLib;
import orison.utils.OrisonHelper;

public class UnstainedChronicle extends AbstractOrisonRelic {

    public static final String ID = makeID(UnstainedChronicle.class.getSimpleName());

    public UnstainedChronicle() {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        for (RewardItem reward : AbstractDungeon.combatRewardScreen.rewards)
            if (reward.cards != null)
                for (AbstractCard c : reward.cards)
                    onPreviewObtainCard(c);
    }

    @Override
    public void onPreviewObtainCard(AbstractCard c) {
        onObtainCard(c);
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        if (OrisonHelper.hasOrisons(c))
            return;
        List<AbstractOrison> orison = OrisonLib.getRandomCommonOrison(false, 1, false);
        if (orison.isEmpty())
            return;
        CardModifierManager.addModifier(c, orison.get(0));
    }
}
