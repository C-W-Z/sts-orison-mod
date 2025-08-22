package orison.ui.components;

import static orison.core.OrisonMod.makeUIPath;

import java.util.function.Consumer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import orison.core.interfaces.ConfigUIElement;
import orison.utils.TexLoader;

public class TextConfig implements ConfigUIElement {

    public static final Texture BG_LEFT = TexLoader.getTexture(makeUIPath("TextConfig/left.png"));
    public static final Texture BG_MIDDLE = TexLoader.getTexture(makeUIPath("TextConfig/middle.png"));
    public static final Texture BG_RIGHT = TexLoader.getTexture(makeUIPath("TextConfig/right.png"));

    public enum Type {
        CHECKBOX,
        HUNDRED_PERCENT_SLIDER
    }

    public static final float PADDING_X = 5 * Settings.xScale;
    public static final float PADDING_Y = 5 * Settings.yScale;

    public float x; // 左側x座標
    public float rightX; // 右側x座標

    private String description;
    private HundredPercentSlider slider;

    private static float text_max_width;
    private static float line_spacing;

    private float textHeight;

    public TextConfig(float x, float rightX, float y, String labal, float value, Consumer<Float> onChange, Type type) {
        this.x = x;
        this.rightX = rightX;
        this.description = labal;
        if (labal == null)
            this.description = "[Missing Text]";

        switch (type) {
            case HUNDRED_PERCENT_SLIDER:
                this.slider = new HundredPercentSlider(rightX - HundredPercentSlider.REAL_W, y, value, onChange);
                break;

            default:
                break;
        }

        line_spacing = Settings.BIG_TEXT_MODE ? (40.0F * Settings.scale) : (32.0F * Settings.scale);
        text_max_width = Settings.isMobile ? (1170.0F * Settings.scale) : (1050.0F * Settings.scale);

        textHeight = FontHelper.getSmartHeight(FontHelper.charDescFont, labal, text_max_width, line_spacing);
    }

    @Override
    public void setTargetY(float targetY) {
        slider.setTargetY(targetY);
    }

    @Override
    public void update() {
        slider.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        float width = rightX - x;
        float height = getHeight();
        float y = slider.getY() - height / 2;

        sb.setColor(Color.DARK_GRAY);
        float bgH = height + 2 * PADDING_Y;
        sb.draw(BG_LEFT, x - PADDING_X, y - PADDING_Y, bgH, bgH);
        sb.draw(BG_MIDDLE, x - PADDING_X + bgH, y - PADDING_Y, width + 2 * PADDING_X - 2 * bgH, bgH);
        sb.draw(BG_RIGHT, x + width + PADDING_X - bgH, y - PADDING_Y, bgH, bgH);


        sb.setColor(Color.WHITE);
        FontHelper.renderFontLeft(sb,
                FontHelper.charDescFont,
                description,
                x + PADDING_X, // 讓左邊是兩倍的padding
                slider.getY(),
                Settings.CREAM_COLOR);
        slider.render(sb);

        if (Settings.isDebug || Settings.isInfo) {
            sb.setColor(Color.RED);
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG,
                    x, y, width, height);
        }
    }

    @Override
    public float getHeight() {
        return Math.max(textHeight, slider.getHeight());
    }
}
