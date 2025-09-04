package orison.ui.screens;

import static orison.core.OrisonMod.makeID;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;

import orison.core.configs.OrisonConfig;
import orison.core.interfaces.ConfigUIElement;
import orison.extensions.vanilla.VanillaExtension;
import orison.ui.components.ConfigScreenBgRenderer;
import orison.ui.components.TextLabel;
import orison.ui.components.panels.GlobalConfigOptionPanel;
import orison.ui.components.panels.OrisonDisplay;

public class OrisonConfigScreen implements ScrollBarListener {

    private static final Logger logger = LogManager.getLogger(OrisonConfigScreen.class);

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(makeID(OrisonConfigScreen.class.getSimpleName())).TEXT;

    public static OrisonConfigScreen instance = null;

    public static final float TOP_BOTTOM_GAP = 2 * Settings.DEFAULT_SCROLL_LIMIT;
    public static final float DRAW_START_X = 400 * Settings.scale;
    public static final float DRAW_END_X = Settings.WIDTH - 250 * Settings.scale - ScrollBar.TRACK_W;
    public static final float DRAW_START_Y = Settings.HEIGHT - TOP_BOTTOM_GAP;
    public static final float ELEMENT_GAP = 100 * Settings.scale;

    public static final float ORISON_DISPLAY_CENTER_X = (350 * Settings.scale + DRAW_END_X) / 2;

    private float scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
    private float scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
    private float scrollY = 0;
    private boolean grabbedScreen = false;
    private float grabStartY = 0F;
    private ScrollBar scrollbar;

    private MenuCancelButton cancelButton;

    /* ===== BACKGROUND ===== */
    public ConfigScreenBgRenderer bgRenderer;

    /* ===== UI ===== */
    private List<ConfigUIElement> scrollables;
    protected float realHeight;
    protected float nextYPos;

    public OrisonConfigScreen() {
        cancelButton = new MenuCancelButton();
        bgRenderer = new ConfigScreenBgRenderer();
        scrollbar = new ScrollBar(this);

        nextYPos = DRAW_START_Y;
        scrollables = new ArrayList<>();
        addOption(new TextLabel(DRAW_START_X, nextYPos, TEXT[1]));
        addOption(new OrisonDisplay(ORISON_DISPLAY_CENTER_X, nextYPos, VanillaExtension.commonOrisons, true));
        addOption(new TextLabel(DRAW_START_X, nextYPos, TEXT[2]));
        addOption(new OrisonDisplay(ORISON_DISPLAY_CENTER_X, nextYPos, VanillaExtension.eventOrisons, false));
        addOption(new TextLabel(DRAW_START_X, nextYPos, TEXT[0]));
        addOption(new GlobalConfigOptionPanel(DRAW_START_X, DRAW_END_X, nextYPos));

        calculateScrollBounds();
    }

    public void addOption(ConfigUIElement element) {
        scrollables.add(element);
        float elementHeight = element.getHeight();
        nextYPos -= elementHeight + ELEMENT_GAP;
        if (scrollables.size() > 1)
            realHeight += ELEMENT_GAP;
        realHeight += elementHeight;
    }

    public void open() {
        OrisonConfig.Version.cancelNotice();
        CardCrawlGame.sound.play("UNLOCK_PING");
        CardCrawlGame.mainMenuScreen.darken();
        cancelButton.show(CardCrawlGame.languagePack.getUIString("DungeonMapScreen").TEXT[1]);
        InputHelper.justClickedLeft = false;
        InputHelper.justReleasedClickLeft = false;
        scrollY = scrollLowerBound;
        for (ConfigUIElement ui : scrollables)
            ui.open();
    }

    public void close() {
        CardCrawlGame.mainMenuScreen.lighten();
        cancelButton.hide();
        CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.MAIN_MENU;
        bgRenderer.close();
    }

    public void update() {
        bgRenderer.update();

        for (ConfigUIElement ui : scrollables)
            ui.updateBeforeScroll();

        boolean isScrollBarScrolling = scrollbar.update();
        if (!OrisonPopup.instance.isOpen && !isScrollBarScrolling)
            updateScrolling();

        float height = 0;
        for (int i = 0; i < scrollables.size(); i++) {
            scrollables.get(i).setTargetY(DRAW_START_Y - height - i * ELEMENT_GAP + scrollY);
            height += scrollables.get(i).getHeight();
            scrollables.get(i).update();
        }

        cancelButton.update();
        if (cancelButton.hb.clicked || InputHelper.pressedEscape) {
            InputHelper.pressedEscape = false;
            cancelButton.hb.clicked = false;
            close();
            return;
        }
    }

    public void render(SpriteBatch sb) {
        bgRenderer.render(sb);

        scrollbar.render(sb);

        for (ConfigUIElement ui : scrollables)
            ui.render(sb);

        cancelButton.render(sb);
    }

    private void updateScrolling() {
        int y = InputHelper.mY;
        if (!grabbedScreen) {
            if (InputHelper.scrolledDown)
                scrollY += Settings.SCROLL_SPEED;
            else if (InputHelper.scrolledUp)
                scrollY -= Settings.SCROLL_SPEED;
            if (InputHelper.justClickedLeft) {
                grabbedScreen = true;
                grabStartY = y - scrollY;
            }
        } else if (InputHelper.isMouseDown) {
            scrollY = y - grabStartY;
        } else {
            grabbedScreen = false;
        }
        resetScrolling();
        updateBarPosition();
    }

    private void calculateScrollBounds() {
        if (realHeight > DRAW_START_Y - TOP_BOTTOM_GAP)
            scrollUpperBound = realHeight - (DRAW_START_Y - TOP_BOTTOM_GAP);
        else
            scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
    }

    private void resetScrolling() {
        if (scrollY < scrollLowerBound)
            scrollY = MathHelper.scrollSnapLerpSpeed(scrollY, scrollLowerBound);
        else if (scrollY > scrollUpperBound)
            scrollY = MathHelper.scrollSnapLerpSpeed(scrollY, scrollUpperBound);
    }

    @Override
    public void scrolledUsingBar(float newPercent) {
        scrollY = MathHelper.valueFromPercentBetween(scrollLowerBound, scrollUpperBound, newPercent);
        updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(scrollLowerBound, scrollUpperBound, scrollY);
        scrollbar.parentScrolledToPercent(percent);
    }

    // TODO: 支援Controller Input
    // private void updateControllerInput() {
    // if (!Settings.isControllerMode)
    // return;
    // selectionIndex = 0;
    // boolean anyHovered = false;
    // // ...
    // }
}
