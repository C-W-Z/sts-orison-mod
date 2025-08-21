package orison.ui.components;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import orison.core.abstracts.AbstractOrison;
import orison.core.interfaces.ConfigUIElement;
import orison.core.libs.OrisonLib;

public class OrisonDisplay implements ConfigUIElement {

    public static final int ORISONS_PER_LINE = 6;
    public static final float ORISON_GAP = 50 * Settings.scale;
    public static final float PAD = OrisonUIElement.SIZE + ORISON_GAP;

    private float drawStartX;
    private float targetY;

    private List<OrisonUIElement> orisonUIs;

    private OrisonUIElement controllerOrison = null;
    private OrisonUIElement hoveredOrison = null;
    private OrisonUIElement clickStartedOrison = null;

    public OrisonDisplay(float draw_start_x, float draw_start_y) {
        orisonUIs = new ArrayList<OrisonUIElement>();

        this.drawStartX = draw_start_x;
        this.targetY = draw_start_y;

        for (int i = 0; i < OrisonLib.allOrisons.size(); i++) {
            AbstractOrison orison = OrisonLib.allOrisons.get(i);

            int xIndex = i % ORISONS_PER_LINE;
            int yIndex = i / ORISONS_PER_LINE;

            float x = draw_start_x + OrisonUIElement.SIZE / 2 + xIndex * PAD;
            float y = draw_start_y - OrisonUIElement.SIZE / 2 - yIndex * PAD;

            orisonUIs.add(new OrisonUIElement(orison, x, y));
        }
        for (int i = 0; i < OrisonLib.allOrisons.size(); i++) {
            AbstractOrison orison = OrisonLib.allOrisons.get(i);

            int xIndex = (i + OrisonLib.allOrisons.size()) % ORISONS_PER_LINE;
            int yIndex = (i + OrisonLib.allOrisons.size()) / ORISONS_PER_LINE;

            float x = draw_start_x + xIndex * PAD;
            float y = draw_start_y - yIndex * PAD;

            orisonUIs.add(new OrisonUIElement(orison.newInstance(true), x, y));
        }
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
                // TODO: 打開OrisonPopup
                // CardCrawlGame.cardPopup.open(clickStartedOrison, visibleCards);
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

        if (Settings.isControllerMode && controllerOrison != null)
            CInputHelper.setCursor(controllerOrison.hb);
    }

    @Override
    public void render(SpriteBatch sb) {
        for (OrisonUIElement orisonUI : orisonUIs)
            orisonUI.render(sb);

        if (Settings.isDebug || Settings.isInfo) {
            sb.setColor(Color.RED);
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG, drawStartX, targetY - getHeight(),
                    ORISONS_PER_LINE * PAD - ORISON_GAP, getHeight());
        }
    }

    @Override
    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    @Override
    public float getHeight() {
        return MathUtils.ceil((float) orisonUIs.size() / ORISONS_PER_LINE) * PAD - ORISON_GAP;
    }
}
