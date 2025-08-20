package orison.ui.screens;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;

import orison.core.abstracts.AbstractOrison;
import orison.core.libs.OrisonLib;
import orison.ui.components.ConfigOptionPanel;
import orison.ui.components.ConfigScreenBgRenderer;
import orison.ui.components.TextConfig;
import orison.ui.components.HundredPercentSlider;
import orison.ui.components.OrisonUIElement;

public class OrisonConfigScreen implements ScrollBarListener {

    private static final Logger logger = LogManager.getLogger(OrisonConfigScreen.class);

    public static OrisonConfigScreen instance = null;

    public static final int ORISONS_PER_LINE = 6;
    public static final float ORISON_GAP = 50 * Settings.scale;
    public static final float PAD = OrisonUIElement.SIZE + ORISON_GAP;

    public static final float DRAW_START_X = 420F * Settings.scale + OrisonUIElement.SIZE / 2F;
    public static final float DRAW_START_Y = Settings.HEIGHT - 200 * Settings.scale;

    private float scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
    private float scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
    private ScrollBar scrollbar;
    private float scrollY = 0;
    private boolean grabbedScreen = false;
    private float grabStartY = 0F;

    private MenuCancelButton cancelButton;
    private List<OrisonUIElement> orisonUIs;

    private OrisonUIElement controllerOrison = null;
    private OrisonUIElement hoveredOrison = null;
    private OrisonUIElement clickStartedOrison = null;

    /* ===== BACKGROUND ===== */
    public ConfigScreenBgRenderer bgRenderer;

    /* ===== UI ===== */
    // private HundredPercentSlider slider;
    private ConfigOptionPanel configUIs;

    public OrisonConfigScreen() {
        cancelButton = new MenuCancelButton();

        bgRenderer = new ConfigScreenBgRenderer();

        scrollbar = new ScrollBar(this);

        // slider = new HundredPercentSlider(1235 * Settings.xScale, Settings.HEIGHT -
        // 200 * Settings.scale, 0.25F, v -> {
        // logger.info("slider: " + MathUtils.round(v * 100F));
        // });
        logger.info("Slider X: " + (TextConfig.DRAW_END_X - HundredPercentSlider.SLIDE_W));
        configUIs = new ConfigOptionPanel(DRAW_START_Y);

        orisonUIs = new ArrayList<OrisonUIElement>();
        for (int i = 0; i < OrisonLib.allOrisons.size(); i++) {
            AbstractOrison orison = OrisonLib.allOrisons.get(i);

            int xIndex = i % ORISONS_PER_LINE;
            int yIndex = i / ORISONS_PER_LINE;

            float x = DRAW_START_X + xIndex * PAD;
            float y = DRAW_START_Y - (configUIs.getHeight() + 2 * ORISON_GAP) - yIndex * PAD;

            orisonUIs.add(new OrisonUIElement(orison, x, y));
        }
        for (int i = 0; i < OrisonLib.allOrisons.size(); i++) {
            AbstractOrison orison = OrisonLib.allOrisons.get(i);

            int xIndex = (i + OrisonLib.allOrisons.size()) % ORISONS_PER_LINE;
            int yIndex = (i + OrisonLib.allOrisons.size()) / ORISONS_PER_LINE;

            float x = DRAW_START_X + xIndex * PAD;
            float y = DRAW_START_Y - (configUIs.getHeight() + 2 * ORISON_GAP) - yIndex * PAD;

            orisonUIs.add(new OrisonUIElement(orison.newInstance(true), x, y));
        }

        calculateScrollBounds();
    }

    public void open() {
        CardCrawlGame.sound.play("UNLOCK_PING");
        CardCrawlGame.mainMenuScreen.darken();
        cancelButton.show(CardCrawlGame.languagePack.getUIString("DungeonMapScreen").TEXT[1]);
        InputHelper.justClickedLeft = false;
        InputHelper.justReleasedClickLeft = false;

        hoveredOrison = null;
        scrollY = scrollLowerBound;
    }

    public void close() {
        CardCrawlGame.mainMenuScreen.lighten();
        cancelButton.hide();
        CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.MAIN_MENU;
        bgRenderer.close();
    }

    public void update() {
        bgRenderer.update();

        if (Settings.isControllerMode && controllerOrison != null && !CardCrawlGame.isPopupOpen) {
            if (Gdx.input.getY() > Settings.HEIGHT * 0.75F)
                scrollY += Settings.SCROLL_SPEED;
            else if (Gdx.input.getY() < Settings.HEIGHT * 0.25F)
                scrollY -= Settings.SCROLL_SPEED;
        }

        if (hoveredOrison != null) {
            CardCrawlGame.cursor.changeType(GameCursor.CursorType.INSPECT);
            if (InputHelper.justClickedLeft)
                clickStartedOrison = hoveredOrison;
            if ((InputHelper.justReleasedClickLeft && clickStartedOrison != null && hoveredOrison != null)
                    || (hoveredOrison != null && CInputActionSet.select.isJustPressed())) {
                if (Settings.isControllerMode)
                    clickStartedOrison = hoveredOrison;
                InputHelper.justReleasedClickLeft = false;
                // TODO: 打開OrisonPopup
                // CardCrawlGame.cardPopup.open(clickStartedOrison, visibleCards);
                clickStartedOrison = null;
            }
        } else {
            clickStartedOrison = null;
        }

        boolean isScrollBarScrolling = scrollbar.update();
        // TODO: 這裡不是用CardCrawlGame.cardPopup，而是自己做的OrisonPopup
        if (!CardCrawlGame.cardPopup.isOpen && !isScrollBarScrolling)
            updateScrolling();

        // slider.setTargetY(DRAW_START_Y + scrollY);
        // slider.update();
        configUIs.setTargetY(DRAW_START_Y + scrollY);
        configUIs.update();
        updateOrisons();

        cancelButton.update();
        if (cancelButton.hb.clicked || InputHelper.pressedEscape) {
            InputHelper.pressedEscape = false;
            cancelButton.hb.clicked = false;
            close();
            return;
        }

        if (Settings.isControllerMode && controllerOrison != null)
            CInputHelper.setCursor(controllerOrison.hb);
    }

    public void render(SpriteBatch sb) {
        bgRenderer.render(sb);

        scrollbar.render(sb);

        // slider.render(sb);
        configUIs.render(sb);

        for (OrisonUIElement orisonUI : orisonUIs)
            orisonUI.render(sb);

        cancelButton.render(sb);
    }

    public void updateOrisons() {
        hoveredOrison = null;
        for (int i = 0; i < orisonUIs.size(); i++) {
            int xIndex = i % ORISONS_PER_LINE;
            int yIndex = i / ORISONS_PER_LINE;
            orisonUIs.get(i).targetX = DRAW_START_X + xIndex * PAD;
            orisonUIs.get(i).targetY = DRAW_START_Y - (configUIs.getHeight() + 2 * ORISON_GAP) + scrollY - yIndex * PAD;
            orisonUIs.get(i).update();
            if (orisonUIs.get(i).hb.hovered)
                hoveredOrison = orisonUIs.get(i);
        }
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
        int size = orisonUIs.size();
        int scrollTmp = 0;
        if (size > ORISONS_PER_LINE * 2) {
            scrollTmp = size / ORISONS_PER_LINE - 2;
            if (size % ORISONS_PER_LINE != 0)
                scrollTmp++;
            scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT + scrollTmp * PAD;
        } else {
            scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        }
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
