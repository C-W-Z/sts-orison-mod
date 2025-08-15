package orison.core.patches;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import javassist.CtBehavior;
import orison.core.rewards.AbstractOrisonReward;
import orison.core.rewards.RandomOrisonReward;

public class OrisonRewardPatch {

    @SpirePatch2(clz = CardRewardScreen.class, method = "takeReward")
    public static class CardRewardScreenPatch {

        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance) {
            if (__instance.rItem instanceof AbstractOrisonReward)
                ((AbstractOrisonReward) __instance.rItem).takeReward();
        }
    }

    @SpirePatch2(clz = MonsterRoomElite.class, method = "dropReward")
    public static class EliteRewardPatch {

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(MonsterRoomElite __instance) {
            RandomOrisonReward reward = new RandomOrisonReward(3);
            if (reward.canAddToRewards())
                __instance.rewards.add(reward);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(MonsterRoomElite.class, "addEmeraldKey");
                int[] lines = LineFinder.findInOrder(ctMethodToPatch, matcher);
                return new int[] { lines[0] + 1 };
            }
        }
    }
}
