package orison.core.patches.fix;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Cauldron;
import com.megacrit.cardcrawl.relics.Orrery;
import com.megacrit.cardcrawl.relics.TinyHouse;

import orison.core.abstracts.AbstractOrisonReward;
import orison.core.patches.RewardLinkPatch.RewardLink;
import orison.core.relics.UnstainedChronicle;

public class RewardFixingPatch {
    @SpirePatches2({
            @SpirePatch2(clz = Cauldron.class, method = "onEquip"),
            @SpirePatch2(clz = Orrery.class, method = "onEquip"),
            @SpirePatch2(clz = TinyHouse.class, method = "onEquip")
    })
    public static class CauldronPatch {
        @SpirePostfixPatch
        public static void Postfix() {
            AbstractDungeon.combatRewardScreen.rewards.forEach(r -> RewardLink.link.set(r, null));
            AbstractDungeon.combatRewardScreen.rewards.removeIf(AbstractOrisonReward.class::isInstance);
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "getRewardCards")
    public static class GetRewardCardsPatch {
        @SpirePostfixPatch
        public static void Postfix(ArrayList<AbstractCard> __result) {
            for (AbstractCard c : __result)
                for (AbstractRelic r : AbstractDungeon.player.relics)
                    if (r instanceof UnstainedChronicle)
                        r.onPreviewObtainCard(c);
        }
    }
}
