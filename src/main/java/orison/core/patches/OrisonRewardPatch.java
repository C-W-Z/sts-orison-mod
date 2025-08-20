package orison.core.patches;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;

import javassist.CtBehavior;
import orison.core.abstracts.AbstractOrisonReward;
import orison.core.configs.OrisonConfig;
import orison.core.rewards.RandomOrisonReward;
import orison.core.savables.OrisonRng;

public class OrisonRewardPatch {

    private static final Logger logger = LogManager.getLogger(OrisonRewardPatch.class);

    @SpirePatch2(clz = CardRewardScreen.class, method = "takeReward")
    public static class CardRewardScreenPatch {

        @SpirePostfixPatch
        public static void Postfix(CardRewardScreen __instance) {
            if (__instance.rItem instanceof AbstractOrisonReward)
                ((AbstractOrisonReward) __instance.rItem).takeReward();
        }
    }

    @SpirePatch2(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class CombatRewardScreenPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CombatRewardScreen __instance) {
            AbstractRoom room = AbstractDungeon.getCurrRoom();

            float chance = 0F;

            if (room instanceof MonsterRoomBoss)
                chance += OrisonConfig.Reward.BOSS_DROP_ORISON_CHANCE;
            else if (room instanceof MonsterRoomElite)
                chance += OrisonConfig.Reward.ELITE_DROP_ORISON_CHANCE;
            else if (room instanceof MonsterRoom)
                chance += OrisonConfig.Reward.MONSTER_DROP_ORISON_CHANCE;

            // Remember OrisonRng counter before any rolled by OrisonRng
            OrisonRng.rememberCounter();

            if (!OrisonRng.get().randomBoolean(chance))
                return;

            if (__instance.rewards.isEmpty()) {
                logger.error("setupItemReward: CombatRewardScreen.rewards is EMPTY");
            } else {
                RewardItem lastReward = __instance.rewards.get(__instance.rewards.size() - 1);
                if (lastReward.cards.size() > 0) {
                    // is card reward
                }
            }

            RandomOrisonReward reward = new RandomOrisonReward(3);
            if (reward.canAddToRewards())
                __instance.rewards.add(reward);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(ProceedButton.class, "show");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}
