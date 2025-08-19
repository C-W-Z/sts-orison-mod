package orison.core.patches;

import static orison.core.OrisonMod.makeID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;

import javassist.CtBehavior;
import orison.core.screens.OrisonMenuScreen;

public class MainMenuPatch {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(Enums.ORISON_BUTTON.name()));

    public static class Enums {
        @SpireEnum
        public static MenuButton.ClickResult ORISON_BUTTON;
        @SpireEnum
        public static MainMenuScreen.CurScreen ORISON_VIEW;
    }

    // Patch to add our button to the menu
    @SpirePatch2(clz = MainMenuScreen.class, method = "setMainMenuButtons")
    public static class ButtonAdderPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = { "index" })
        public static void setMainMenuButtons(MainMenuScreen __instance, @ByRef int[] index) {
            // Add our button at the current index and increment
            __instance.buttons.add(new MenuButton(Enums.ORISON_BUTTON, index[0]));
            index[0]++;
        }

        // Target location to insert our button
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(Settings.class, "isShowBuild");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    // Patch to set the label text for our button
    @SpirePatch2(clz = MenuButton.class, method = "setLabel")
    public static class SetText {
        @SpirePostfixPatch
        public static void setLabelText(MenuButton __instance, @ByRef String[] ___label) {
            if (__instance.result == Enums.ORISON_BUTTON)
                ___label[0] = uiStrings.TEXT[0];
        }
    }

    // Patch to handle clicks on our button
    @SpirePatch2(clz = MenuButton.class, method = "buttonEffect")
    public static class OnClickButton {
        @SpirePostfixPatch
        public static void handleClick(MenuButton __instance) {
            if (__instance.result == Enums.ORISON_BUTTON) {
                CardCrawlGame.sound.play("UNLOCK_PING");
                // Open the Orison screen
                if (OrisonMenuScreen.instance == null) {
                    OrisonMenuScreen.instance = new OrisonMenuScreen();
                }
                OrisonMenuScreen.instance.open();
                CardCrawlGame.mainMenuScreen.screen = Enums.ORISON_VIEW;
            }
        }
    }

    // Patch to handle SpirePass screen updates
    @SpirePatch2(clz = MainMenuScreen.class, method = "update")
    public static class UpdatePatch {
        @SpirePostfixPatch
        public static void updateSpirePass(MainMenuScreen __instance) {
            if (__instance.screen == Enums.ORISON_VIEW) {
                OrisonMenuScreen.instance.update();
            }
        }
    }

    // Patch to handle SpirePass screen rendering
    @SpirePatch2(clz = MainMenuScreen.class, method = "render")
    public static class RenderPatch {
        @SpirePostfixPatch
        public static void renderSpirePass(MainMenuScreen __instance, SpriteBatch sb) {
            if (__instance.screen == Enums.ORISON_VIEW) {
                OrisonMenuScreen.instance.render(sb);
            }
        }
    }
}
