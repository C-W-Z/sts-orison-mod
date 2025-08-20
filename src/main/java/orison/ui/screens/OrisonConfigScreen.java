package orison.ui.screens;

import static orison.core.OrisonMod.makeUIPath;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;

import orison.core.abstracts.AbstractOrison;
import orison.core.configs.OrisonConfig;
import orison.core.libs.OrisonLib;
import orison.ui.components.ConfigOptionPanel;
import orison.ui.components.ConfigSlider;
import orison.ui.components.HundredPercentSlider;
import orison.ui.components.OrisonUIElement;
import orison.utils.TexLoader;

public class OrisonConfigScreen implements ScrollBarListener {

    private static final Logger logger = LogManager.getLogger(OrisonConfigScreen.class);

    public static OrisonConfigScreen instance = null;

    public static final int ORISONS_PER_LINE = 6;
    public static final float ORISON_GAP = 50 * Settings.scale;
    public static final float PAD = OrisonUIElement.SIZE + ORISON_GAP;

    public static final float DRAW_START_X = 420F * Settings.scale + OrisonUIElement.SIZE / 2F;
    public static final float DRAW_START_Y = Settings.HEIGHT / 2F;

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
    public static final int MAX_BG_INDEX = 6;
    public static final float BG_CHOICE_ARROW_GAP_X = 50 * Settings.scale;
    public static final float BG_CHOICE_CX = 150 * Settings.scale;
    public static final float BG_CHOICE_CY = Settings.HEIGHT - 100 * Settings.scale;
    private List<Texture> bgTextures;
    private int currentBgIndex = 0;
    private Texture bg;
    private Hitbox prevBgHb;
    private Hitbox nextBgHb;

    /* ===== UI ===== */
    // private HundredPercentSlider slider;
    private ConfigOptionPanel configUIs;

    public OrisonConfigScreen() {
        cancelButton = new MenuCancelButton();

        bgTextures = new ArrayList<>();
        for (int i = 0; i <= MAX_BG_INDEX; i++)
            bgTextures.add(null);
        currentBgIndex = OrisonConfig.Preference.CONFIG_SCREEN_BG;
        if (currentBgIndex != 0) {
            bg = TexLoader.getTexture(makeUIPath("OrisonConfigScreen/bg/" + currentBgIndex + ".png"));
            bgTextures.set(currentBgIndex, bg);
        } else {
            bg = null;
        }
        prevBgHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        nextBgHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        prevBgHb.move(BG_CHOICE_CX - BG_CHOICE_ARROW_GAP_X, BG_CHOICE_CY);
        nextBgHb.move(BG_CHOICE_CX + BG_CHOICE_ARROW_GAP_X, BG_CHOICE_CY);

        scrollbar = new ScrollBar(this);

        // slider = new HundredPercentSlider(1235 * Settings.xScale, Settings.HEIGHT -
        // 200 * Settings.scale, 0.25F, v -> {
        // logger.info("slider: " + MathUtils.round(v * 100F));
        // });
        logger.info("Slider X: " + (ConfigSlider.DRAW_END_X - HundredPercentSlider.SLIDE_W));
        configUIs = new ConfigOptionPanel(Settings.HEIGHT - 200 * Settings.scale);

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
    }

    public void update() {

        updateBackgroundChoice();

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

        // slider.setTargetY(Settings.HEIGHT - 200 * Settings.scale + scrollY);
        // slider.update();
        configUIs.setTargetY(Settings.HEIGHT - 200 * Settings.scale + scrollY);
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
        renderBackground(sb);
        renderBackgroundChoice(sb);

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
            orisonUIs.get(i).targetY = DRAW_START_Y + scrollY - yIndex * PAD;
            orisonUIs.get(i).update();
            if (orisonUIs.get(i).hb.hovered)
                hoveredOrison = orisonUIs.get(i);
        }
    }

    private void prevBackground() {
        // currentBgIndex = (currentBgIndex-1 + MAX_BG_INDEX+1) % (MAX_BG_INDEX+1);
        currentBgIndex = (currentBgIndex + MAX_BG_INDEX) % (MAX_BG_INDEX + 1);
        bg = bgTextures.get(currentBgIndex);
        if (bg == null && currentBgIndex != 0) {
            bg = TexLoader.getTexture(makeUIPath("OrisonConfigScreen/bg/" + currentBgIndex + ".png"));
            bgTextures.set(currentBgIndex, bg);
        }
    }

    private void nextBackground() {
        currentBgIndex = (currentBgIndex + 1) % (MAX_BG_INDEX + 1);
        bg = bgTextures.get(currentBgIndex);
        if (bg == null && currentBgIndex != 0) {
            bg = TexLoader.getTexture(makeUIPath("OrisonConfigScreen/bg/" + currentBgIndex + ".png"));
            bgTextures.set(currentBgIndex, bg);
        }
    }

    private void updateBackgroundChoice() {
        prevBgHb.update();
        nextBgHb.update();

        if (prevBgHb.clicked) {
            prevBgHb.clicked = false;
            CardCrawlGame.sound.play("UI_CLICK_1");
            prevBackground();
            OrisonConfig.Preference.save(OrisonConfig.Preference.ID_CONFIG_SCREEN_BG, currentBgIndex);
        }

        if (nextBgHb.clicked) {
            nextBgHb.clicked = false;
            CardCrawlGame.sound.play("UI_CLICK_1");
            nextBackground();
            OrisonConfig.Preference.save(OrisonConfig.Preference.ID_CONFIG_SCREEN_BG, currentBgIndex);
        }

        if (InputHelper.justClickedLeft) {
            if (prevBgHb.hovered)
                prevBgHb.clickStarted = true;
            if (nextBgHb.hovered)
                nextBgHb.clickStarted = true;
        }
    }

    private void renderBackgroundChoice(SpriteBatch sb) {
        sb.setColor(Color.WHITE);

        FontHelper.renderFontCentered(
                sb,
                FontHelper.cardTitleFont,
                "Background",
                BG_CHOICE_CX,
                BG_CHOICE_CY + 50,
                Settings.GOLD_COLOR,
                1.25F);

        FontHelper.renderFontCentered(
                sb,
                FontHelper.cardTitleFont,
                String.valueOf(currentBgIndex),
                BG_CHOICE_CX,
                BG_CHOICE_CY,
                Settings.GOLD_COLOR);

        if (prevBgHb.hovered)
            sb.setColor(Color.LIGHT_GRAY);
        else
            sb.setColor(Color.WHITE);

        sb.draw(ImageMaster.CF_LEFT_ARROW, prevBgHb.cX - 24.0F, prevBgHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F,
                Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

        if (nextBgHb.hovered)
            sb.setColor(Color.LIGHT_GRAY);
        else
            sb.setColor(Color.WHITE);

        sb.draw(ImageMaster.CF_RIGHT_ARROW, nextBgHb.cX - 24.0F, nextBgHb.cY - 24.0F, 24.0F, 24.0F, 48.0F,
                48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

        prevBgHb.render(sb);
        nextBgHb.render(sb);
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
