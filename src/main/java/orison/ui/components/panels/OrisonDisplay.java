package orison.ui.components.panels;

import static orison.core.OrisonMod.makeID;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import orison.core.interfaces.ConfigUIElement;
import orison.core.libs.OrisonLib;
import orison.ui.components.OrisonUIElement;
import orison.ui.screens.OrisonPopup;

public class OrisonDisplay implements ConfigUIElement {

    public static final String[] TEXT = CardCrawlGame.languagePack
            .getUIString(makeID(OrisonDisplay.class.getSimpleName())).TEXT;

    public static final int ORISONS_PER_LINE = 6;
    public static final float ORISON_GAP = 50 * Settings.scale;
    public static final float PAD = OrisonUIElement.SIZE + ORISON_GAP;
    public static final float WIDTH = ORISONS_PER_LINE * PAD - ORISON_GAP;

    private float drawStartX;
    private float targetY;

    private List<OrisonUIElement> orisonUIs;

    private OrisonUIElement controllerOrison = null;
    private OrisonUIElement hoveredOrison = null;
    private OrisonUIElement clickStartedOrison = null;

    public static final float ADV_TOGGLE_CENTER_X = 150 * Settings.scale;
    public static final float ADV_TOGGLE_CENTER_Y = 300 * Settings.scale;
    private Hitbox upgradeHb = new Hitbox(250.0F * Settings.scale, 80.0F * Settings.scale);

    public OrisonDisplay(float centerX, float drawStartY) {
        orisonUIs = new ArrayList<OrisonUIElement>();

        this.drawStartX = centerX - WIDTH / 2;
        this.targetY = drawStartY;

        for (int i = 0; i < OrisonLib.allOrisons.size(); i++) {
            int xIndex = i % ORISONS_PER_LINE;
            int yIndex = i / ORISONS_PER_LINE;
            float x = drawStartX + OrisonUIElement.SIZE / 2 + xIndex * PAD;
            float y = drawStartY - OrisonUIElement.SIZE / 2 - yIndex * PAD;
            orisonUIs.add(new OrisonUIElement(OrisonLib.allOrisons.get(i), x, y));
        }

        this.upgradeHb.move(ADV_TOGGLE_CENTER_X, ADV_TOGGLE_CENTER_Y);
    }

    public void open() {
        hoveredOrison = null;
    }

    public void updateBeforeScroll() {
        if (hoveredOrison != null) {
            CardCrawlGame.cursor.changeType(GameCursor.CursorType.INSPECT);
            if (InputHelper.justClickedLeft)
                clickStartedOrison = hoveredOrison;
            if ((InputHelper.justReleasedClickLeft && clickStartedOrison != null && hoveredOrison != null)
                    || (hoveredOrison != null && CInputActionSet.select.isJustPressed())) {
                if (Settings.isControllerMode)
                    clickStartedOrison = hoveredOrison;
                InputHelper.justReleasedClickLeft = false;
                OrisonPopup.instance.open(clickStartedOrison.orison, OrisonLib.allOrisons);
                clickStartedOrison = null;
            }
        } else {
            clickStartedOrison = null;
        }
    }

    @Override
    public void update() {
        hoveredOrison = null;
        for (int i = 0; i < orisonUIs.size(); i++) {
            int xIndex = i % ORISONS_PER_LINE;
            int yIndex = i / ORISONS_PER_LINE;
            orisonUIs.get(i).targetX = drawStartX + OrisonUIElement.SIZE / 2 + xIndex * PAD;
            orisonUIs.get(i).targetY = targetY - OrisonUIElement.SIZE / 2 - yIndex * PAD;
            orisonUIs.get(i).update();
            if (orisonUIs.get(i).hb.hovered)
                hoveredOrison = orisonUIs.get(i);
        }

        updateUpgradePreview();

        if (Settings.isControllerMode && controllerOrison != null)
            CInputHelper.setCursor(controllerOrison.hb);
    }

    protected void updateUpgradePreview() {
        upgradeHb.update();
        if (upgradeHb.hovered && InputHelper.justClickedLeft)
            upgradeHb.clickStarted = true;
        if (upgradeHb.clicked || CInputActionSet.proceed.isJustPressed()) {
            CInputActionSet.proceed.unpress();
            upgradeHb.clicked = false;
            OrisonPopup.isViewingUpgrade = !OrisonPopup.isViewingUpgrade;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        for (OrisonUIElement orisonUI : orisonUIs)
            orisonUI.render(sb);

        renderUpgradeViewToggle(sb);
        if (Settings.isControllerMode)
            sb.draw(CInputActionSet.proceed
                    .getKeyImg(), upgradeHb.cX - 132.0F * Settings.scale - 32.0F,
                    -32.0F + 67.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale,
                    0.0F, 0, 0, 64, 64, false, false);

        if (Settings.isDebug || Settings.isInfo) {
            sb.setColor(Color.RED);
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG, drawStartX, targetY - getHeight(),
                    WIDTH, getHeight());
        }
    }

    protected void renderUpgradeViewToggle(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.CHECKBOX, upgradeHb.cX - 80.0F * Settings.scale - 32.0F, upgradeHb.cY - 32.0F,
                32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);

        Color upgradeToggleColor = upgradeHb.hovered ? Settings.BLUE_TEXT_COLOR : Settings.GOLD_COLOR;
        FontHelper.renderFont(sb, FontHelper.cardTitleFont, TEXT[0], upgradeHb.cX - 45.0F * Settings.scale,
                upgradeHb.cY + 10.0F * Settings.scale, upgradeToggleColor);

        if (OrisonPopup.isViewingUpgrade) {
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.TICK, upgradeHb.cX - 80.0F * Settings.scale - 32.0F, upgradeHb.cY - 32.0F,
                    32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }
        upgradeHb.render(sb);
    }

    @Override
    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    @Override
    public float getHeight() {
        return MathUtils.ceil((float) orisonUIs.size() / ORISONS_PER_LINE) * PAD - ORISON_GAP;
    }

    @Override
    public float getY() {
        return targetY;
    }
}
