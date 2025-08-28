package orison.core.patches;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.AbstractStance;

import basemod.helpers.CardModifierManager;
import javassist.CtBehavior;
import orison.cardmodifiers.ChangeCostUntilUseModifier;

public class PreparePatch {

    private static final Logger logger = LogManager.getLogger(PreparePatch.class);

    @SpirePatch2(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class Field {
        public static SpireField<Integer> prepare = new SpireField<>(() -> 0);
        public static SpireField<Boolean> canTriggerPrepare = new SpireField<>(() -> true);
    }

    public static void triggerPrepare(AbstractCard card) {
        Boolean canTriggerPrepare = Field.canTriggerPrepare.get(card);
        if (canTriggerPrepare == null || canTriggerPrepare == false)
            return;
        Integer prepare = Field.prepare.get(card);
        if (prepare == null || prepare <= 0)
            return;
        logger.info("triggerPrepare: " + card.cardID);
        CardModifierManager.addModifier(card, new ChangeCostUntilUseModifier(-prepare));
    }

    public static void setCanTriggerPrepareOnUseCard(AbstractCard card) {
        logger.debug("setCanTriggerPrepareOnUseCard: " + card.cardID);
        // bad_silent的牌"Sleight of Hand"效果是打出時觸發棄牌效果
        if (card.cardID.equals("bad_silent:Sleight"))
            return;
        Field.canTriggerPrepare.set(card, false);
    }

    @SpirePatch2(clz = GameActionManager.class, method = "callEndOfTurnActions")
    public static class TriggerOnEndOfTurnPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert() {
            logger.debug("Start triggerPrepare from callEndOfTurnActions");
            for (AbstractCard c : AbstractDungeon.player.hand.group)
                triggerPrepare(c);
            logger.debug("End triggerPrepare from callEndOfTurnActions");
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractStance.class, "onEndOfTurn");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch2(clz = UseCardAction.class, method = "update")
    public static class UseCardActionPatch {

        @SpireInsertPatch(locator = Locator1.class)
        public static void Insert1(AbstractCard ___targetCard) {
            setCanTriggerPrepareOnUseCard(___targetCard);
        }

        private static class Locator1 extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(UseCardAction.class, "reboundCard");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }

        @SpireInsertPatch(locator = Locator2.class)
        public static void Insert2(AbstractCard ___targetCard) {
            Field.canTriggerPrepare.set(___targetCard, true);
        }

        private static class Locator2 extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "exhaustOnUseOnce");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch2(clz = CardGroup.class, method = "moveToDiscardPile")
    public static class MoveToDiscardPilePatch {
        @SpirePostfixPatch
        public static void Postfix(CardGroup __instance, AbstractCard c) {
            if (__instance.type == CardGroupType.HAND || __instance.type == CardGroupType.DRAW_PILE) {
                logger.info("triggerPrepare from moveToDiscardPile: " + c.cardID);
                triggerPrepare(c);
            }
            // 只是避免UseCardAction中沒有重設到（例如被其他mod patch掉提前return）
            Field.canTriggerPrepare.set(c, true);
        }
    }
}
