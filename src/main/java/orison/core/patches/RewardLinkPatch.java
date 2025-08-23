package orison.core.patches;

import static orison.core.OrisonMod.makeID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardItem.RewardType;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomReward;
import javassist.CtBehavior;
import orison.core.abstracts.AbstractOrisonReward;

/** Only support links between Orison Reward & Card Reward */
public class RewardLinkPatch {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(makeID(RewardLinkPatch.class.getSimpleName())).TEXT;

    @SpirePatch(clz = RewardItem.class, method = SpirePatch.CLASS)
    public static class RewardLink {
        public static SpireField<RewardItem> link = new SpireField<RewardItem>(() -> null);
    }

    /** Only support links between Orison Reward & Card Reward */
    public static void setRewardLink(RewardItem orisonReward, RewardItem cardReward) {
        RewardLink.link.set(orisonReward, cardReward);
        RewardLink.link.set(cardReward, orisonReward);
    }

    @SpirePatch2(clz = RewardItem.class, method = "update")
    public static class UpdatePatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(RewardItem __instance) {
            if (RewardLink.link.get(__instance) != null)
                RewardLink.link.get(__instance).redText = __instance.hb.hovered;
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(RewardItem.class, "relicLink");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    /** 插入到card reward */
    @SpirePatch2(clz = RewardItem.class, method = "render")
    public static class RenderCardRewardTipPatch {
        @SpireInsertPatch(locator = LocatorCard.class)
        public static void InsertCard(RewardItem __instance) {
            if (__instance.type != RewardType.CARD || !__instance.hb.hovered || RewardLink.link.get(__instance) == null)
                return;
            TipHelper.renderGenericTip(360.0F * Settings.scale, InputHelper.mY + 50.0F * Settings.scale,
                    TEXT[0], TEXT[1] + FontHelper.colorString(TEXT[2], "y") + TEXT[4]);
        }

        private static class LocatorCard extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(RewardItem.class, "isBoss");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch2(clz = CustomReward.class, method = "render")
    public static class RenderOrisonRewardTipPatch {
        @SpirePostfixPatch
        public static void Postfix(RewardItem __instance, SpriteBatch sb) {
            if (!(__instance instanceof AbstractOrisonReward) || RewardLink.link.get(__instance) == null)
                return;
            if (__instance.hb.hovered)
                TipHelper.renderGenericTip(360.0F * Settings.scale, InputHelper.mY + 50.0F * Settings.scale,
                        TEXT[0], TEXT[1] + FontHelper.colorString(TEXT[3], "y") + TEXT[4]);
            // 畫連結的鎖鏈圖示
            ReflectionHacks.privateMethod(RewardItem.class, "renderRelicLink", SpriteBatch.class)
                    .invoke(__instance, sb);
        }
    }

    @SpirePatch2(clz = CardRewardScreen.class, method = "takeReward")
    public static class AcquireCardPatch {
        @SpirePrefixPatch
        public static void Insert(CardRewardScreen __instance) {
            if (__instance.rItem == null || RewardLink.link.get(__instance.rItem) == null)
                return;
            // RewardLink.link.get(__instance.rItem).isDone = true;
            // RewardLink.link.get(__instance.rItem).ignoreReward = true;
            AbstractDungeon.combatRewardScreen.rewards.remove(RewardLink.link.get(__instance.rItem));
        }
    }
}
