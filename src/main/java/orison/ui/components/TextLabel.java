package orison.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;

import orison.core.interfaces.ConfigUIElement;

public class TextLabel implements ConfigUIElement {

    public static final float PADDING_X = 5 * Settings.xScale;

    private float x; // 左側x座標
    private float cY; // 中心y座標
    public float targetY;

    private String description;

    private static float text_max_width;
    private static float line_spacing;

    private float textHeight;

    public TextLabel(float x, float y, String labal) {
        this.x = x;
        this.cY = y;
        this.description = labal;
        if (labal == null)
            this.description = "[Missing Text]";

        line_spacing = Settings.BIG_TEXT_MODE ? (40.0F * Settings.scale) : (32.0F * Settings.scale);
        text_max_width = Settings.isMobile ? (1170.0F * Settings.scale) : (1050.0F * Settings.scale);

        textHeight = FontHelper.getSmartHeight(FontHelper.SCP_cardTitleFont_small, labal, text_max_width, line_spacing);
    }

    @Override
    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    @Override
    public void update() {
        cY = MathHelper.cardLerpSnap(cY, targetY);
    }

    @Override
    public void render(SpriteBatch sb) {
        float height = getHeight();
        float y = cY - height / 2;

        sb.setColor(Color.WHITE);
        FontHelper.renderFontLeft(sb,
                FontHelper.SCP_cardTitleFont_small,
                description,
                x,
                cY,
                Settings.GOLD_COLOR);

        if (Settings.isDebug || Settings.isInfo) {
            sb.setColor(Color.RED);
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG,
                    x, y, text_max_width, height);
        }
    }

    @Override
    public float getHeight() {
        return textHeight;
    }

    @Override
    public float getY() {
        return cY;
    }
}
