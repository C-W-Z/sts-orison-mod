package orison.core.screens;

import static orison.core.OrisonMod.makeUIPath;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
import orison.utils.TexLoader;

public class OrisonMenuScreen implements ScrollBarListener {

    public static OrisonMenuScreen instance = null;

    public static final String BG_URL = makeUIPath("OrisonMenuScreen/BG.png");

    public static final int ORISONS_PER_LINE = 6;
    public static final float ORISON_GAP = 50 * Settings.scale;
    public static final float PAD = OrisonUIElement.SIZE + ORISON_GAP;
    public static final float DRAW_START_X = 420F * Settings.scale + OrisonUIElement.SIZE / 2F;
    public static final float DRAW_START_Y = Settings.HEIGHT / 2F;

    private float scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
    private float scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;

    private Texture bg;

    private MenuCancelButton cancelButton;
    private List<OrisonUIElement> orisonUIs;

    private ScrollBar scrollbar;
    private float currentDiffY = 0;

    private boolean grabbedScreen = false;
    private float grabStartY = 0F;

    private OrisonUIElement controllerOrison = null;
    private OrisonUIElement hoveredOrison = null;
    private OrisonUIElement clickStartedOrison = null;

    public OrisonMenuScreen() {
        cancelButton = new MenuCancelButton();

        bg = TexLoader.getTexture(BG_URL);

        scrollbar = new ScrollBar(this);

        orisonUIs = new ArrayList<OrisonUIElement>();
        for (int i = 0; i < OrisonLib.allOrisons.size(); i++) {
            AbstractOrison orison = OrisonLib.allOrisons.get(i);

            int xIndex = i % ORISONS_PER_LINE;
            int yIndex = i / ORISONS_PER_LINE;

            float x = DRAW_START_X + xIndex * PAD;
            float y = DRAW_START_Y - yIndex * PAD;

            orisonUIs.add(new OrisonUIElement(orison, x, y));
        }

        calculateScrollBounds();
    }

    public void open() {
        cancelButton.show(CardCrawlGame.languagePack.getUIString("DungeonMapScreen").TEXT[1]);
        InputHelper.justClickedLeft = false;
        InputHelper.justReleasedClickLeft = false;

        hoveredOrison = null;
        currentDiffY = scrollLowerBound;
    }

    public void close() {
        CardCrawlGame.mainMenuScreen.lighten();
        cancelButton.hide();
        CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.MAIN_MENU;
    }

    public void update() {

        if (Settings.isControllerMode && controllerOrison != null && !CardCrawlGame.isPopupOpen) {
            if (Gdx.input.getY() > Settings.HEIGHT * 0.75F)
                currentDiffY += Settings.SCROLL_SPEED;
            else if (Gdx.input.getY() < Settings.HEIGHT * 0.25F)
                currentDiffY -= Settings.SCROLL_SPEED;
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
        renderBackground(sb);

        scrollbar.render(sb);

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
            orisonUIs.get(i).targetY = DRAW_START_Y + currentDiffY - yIndex * PAD;
            orisonUIs.get(i).update();
            if (orisonUIs.get(i).hb.hovered)
                hoveredOrison = orisonUIs.get(i);
        }
    }

    private void renderBackground(SpriteBatch sb) {
        if (bg == null)
            return;
        sb.setColor(Color.WHITE);

        float screenW = Settings.WIDTH;
        float screenH = Settings.HEIGHT;

        float imgW = bg.getWidth();
        float imgH = bg.getHeight();

        float scaleX = screenW / imgW;
        float scaleY = screenH / imgH;

        float coverScale = Math.max(scaleX, scaleY);

        float scaledW = imgW * coverScale;
        float scaledH = imgH * coverScale;

        // 中心對齊
        float drawX = (screenW - scaledW) / 2f;
        float drawY = (screenH - scaledH) / 2f;

        sb.draw(bg, drawX, drawY, scaledW, scaledH);
    }

    private void updateScrolling() {
        int y = InputHelper.mY;
        if (!grabbedScreen) {
            if (InputHelper.scrolledDown)
                currentDiffY += Settings.SCROLL_SPEED;
            else if (InputHelper.scrolledUp)
                currentDiffY -= Settings.SCROLL_SPEED;
            if (InputHelper.justClickedLeft) {
                grabbedScreen = true;
                grabStartY = y - currentDiffY;
            }
        } else if (InputHelper.isMouseDown) {
            currentDiffY = y - grabStartY;
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
        if (currentDiffY < scrollLowerBound)
            currentDiffY = MathHelper.scrollSnapLerpSpeed(currentDiffY, scrollLowerBound);
        else if (currentDiffY > scrollUpperBound)
            currentDiffY = MathHelper.scrollSnapLerpSpeed(currentDiffY, scrollUpperBound);
    }

    @Override
    public void scrolledUsingBar(float newPercent) {
        currentDiffY = MathHelper.valueFromPercentBetween(scrollLowerBound, scrollUpperBound,
                newPercent);
        updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(scrollLowerBound, scrollUpperBound,
                currentDiffY);
        scrollbar.parentScrolledToPercent(percent);
    }

    // TODO: 支援Controller Input
    // private void updateControllerInput() {
    //     if (!Settings.isControllerMode)
    //         return;
    //     selectionIndex = 0;
    //     boolean anyHovered = false;
    //     // ...
    // }
}
