package orison.ui.components;

import java.util.function.Consumer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;

import orison.core.interfaces.ConfigUIElement;

public class TextConfig implements ConfigUIElement {

    public enum Type {
        CHECKBOX,
        HUNDRED_PERCENT_SLIDER
    }

    public static final float DRAW_START_X = 400F * Settings.scale;
    public static final float DRAW_END_X = Settings.WIDTH - 375F * Settings.scale - ScrollBar.TRACK_W / 2F;

    private String description;
    private HundredPercentSlider slider;

    private static float text_max_width;
    private static float line_spacing;

    private float textHeight;

    public TextConfig(float y, String labal, float value, Consumer<Float> onChange, Type type) {
        this.description = labal;
        if (labal == null)
            this.description = "Something went wrong here";

        switch (type) {
            case HUNDRED_PERCENT_SLIDER:
                this.slider = new HundredPercentSlider(DRAW_END_X - HundredPercentSlider.SLIDE_W, y, value, onChange);
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
        FontHelper.renderFontLeft(sb,
                FontHelper.charDescFont,
                description,
                DRAW_START_X,
                slider.getY(),
                Settings.CREAM_COLOR);
        slider.render(sb);
    }

    @Override
    public float getHeight() {
        return Math.max(textHeight, slider.getHeight());
    }
}
