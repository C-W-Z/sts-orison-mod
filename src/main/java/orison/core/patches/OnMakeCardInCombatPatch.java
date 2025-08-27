package orison.core.patches;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.MakeTempCardAtBottomOfDeckAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.actions.utility.ChooseOneColorless;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import orison.core.relics.OrganicForm;

public class OnMakeCardInCombatPatch {

    public static void checkAndAttachOrison(AbstractCard c) {
        if (AbstractDungeon.player.hasRelic(OrganicForm.ID))
            OrganicForm.onCardMakeInBattle(c);
    }

    @SpirePatch2(clz = MakeTempCardAtBottomOfDeckAction.class, method = "update")
    public static class MakeTempCardAtBottomOfDeckActionPatch {
        @SpireInstrumentPatch
        public static ExprEditor instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals("com.megacrit.cardcrawl.cards.CardGroup")
                            && m.getMethodName().equals("addToBottom")) {
                        // 在呼叫 addToBottom 前插入
                        m.replace("{ " +
                                OnMakeCardInCombatPatch.class.getName() + ".checkAndAttachOrison($1); " +
                                "$_ = $proceed($$); " +
                                " }");
                    }
                }
            };
        }
    }

    @SpirePatch2(clz = MakeTempCardInDiscardAction.class, method = SpirePatch.CLASS)
    public static class MakeTempCardInDiscardActionFeild {
        public static SpireField<Boolean> shouldAttachOrison = new SpireField<>(() -> false);
    }

    @SpirePatch2(clz = MakeTempCardInDiscardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {
            AbstractCard.class, boolean.class
    })
    public static class MakeTempCardInDiscardActionPatch {
        @SpirePostfixPatch
        public static void Postfix(MakeTempCardInDiscardAction __instance, AbstractCard card, boolean sameUUID) {
            MakeTempCardInDiscardActionFeild.shouldAttachOrison.set(__instance, !sameUUID);
        }
    }

    @SpirePatch2(clz = MakeTempCardInDiscardAction.class, method = "makeNewCard")
    public static class MakeTempCardInDiscardActionPatch2 {
        @SpirePostfixPatch
        public static AbstractCard Postfix(AbstractCard __result, MakeTempCardInDiscardAction __instance) {
            if (!MakeTempCardInDiscardActionFeild.shouldAttachOrison.get(__instance))
                return __result;
            checkAndAttachOrison(__result);
            return __result;
        }
    }

    @SpirePatch(clz = MakeTempCardInDrawPileAction.class, method = "update")
    public static class MakeTempCardInDrawPileActionPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals("com.megacrit.cardcrawl.cards.AbstractCard")
                            && m.getMethodName().equals("makeStatEquivalentCopy")) {
                        m.replace("{ " +
                                " $_ = $proceed($$); " +
                                OnMakeCardInCombatPatch.class.getName() + ".checkAndAttachOrison($_); " +
                                " }");
                    }
                }
            };
        }
    }

    @SpirePatch2(clz = MakeTempCardInHandAction.class, method = "makeNewCard")
    public static class MakeTempCardInHandActionPatch {
        @SpirePostfixPatch
        public static AbstractCard Postfix(AbstractCard __result, MakeTempCardInHandAction __instance) {
            checkAndAttachOrison(__result);
            return __result;
        }
    }

    @SpirePatch2(clz = DiscoveryAction.class, method = "update")
    public static class DiscoveryActionPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = { "disCard", "disCard2" })
        public static void Insert(AbstractCard disCard, AbstractCard disCard2) {
            checkAndAttachOrison(disCard);
            checkAndAttachOrison(disCard2);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasPower");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch2(clz = ChooseOneColorless.class, method = "update")
    public static class ChooseOneColorlessPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = { "disCard" })
        public static void Insert(AbstractCard disCard) {
            checkAndAttachOrison(disCard);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasPower");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch2(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {
            AbstractCard.class, float.class, float.class
    })
    public static class ShowCardAndAddToDiscardEffectPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractCard srcCard) {
            checkAndAttachOrison(srcCard);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(CardGroup.class, "addToTop");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch2(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {
            AbstractCard.class
    })
    public static class ShowCardAndAddToDiscardEffectPatch2 {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractCard card) {
            checkAndAttachOrison(card);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(CardGroup.class, "addToTop");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch2(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {
            AbstractCard.class, float.class, float.class, boolean.class, boolean.class, boolean.class
    })
    public static class ShowCardAndAddToDrawPileEffectPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractCard ___card) {
            checkAndAttachOrison(___card);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(SoundMaster.class, "play");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatches2({
            @SpirePatch2(clz = ShowCardAndAddToHandEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {
                    AbstractCard.class, float.class, float.class
            }),
            @SpirePatch2(clz = ShowCardAndAddToHandEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {
                    AbstractCard.class
            })
    })
    public static class ShowCardAndAddToHandEffectPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractCard ___card) {
            checkAndAttachOrison(___card);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(CardGroup.class, "addToHand");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}
