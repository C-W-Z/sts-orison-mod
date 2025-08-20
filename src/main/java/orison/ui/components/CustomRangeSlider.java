package orison.ui.components;

import static orison.utils.GeneralUtils.clamp;

import java.text.DecimalFormat;
import java.util.function.Consumer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import orison.core.interfaces.ConfigUIElement;

public class CustomRangeSlider implements ConfigUIElement {

    public static final float SLIDE_W = 230.0F * Settings.xScale;

    private float BG_X;
    private float L_X;
    private float x;
    private float y;

    private float value; // 實際值
    private float minValue;
    private float maxValue;

    private int precision; // 小數點精度

    public Hitbox hb;
    public Hitbox bgHb;
    private boolean sliderGrabbed = false;

    private DecimalFormat df;
    private Consumer<Float> onChange;

    private float targetY;

    public CustomRangeSlider(float x, float y,
            float initialValue,
            float minValue,
            float maxValue,
            int precision,
            boolean isPercent,
            Consumer<Float> onChange) {

        this.L_X = x;
        this.BG_X = L_X + 115 * Settings.xScale;

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.precision = precision;
        this.onChange = onChange;

        this.value = clamp(initialValue, minValue, maxValue);

        this.hb = new Hitbox(42.0F * Settings.scale, 38.0F * Settings.scale);
        this.bgHb = new Hitbox(300.0F * Settings.scale, 38.0F * Settings.scale);
        this.bgHb.move(BG_X, y);

        this.x = L_X + SLIDE_W * getNormalizedValue();
        this.y = y;
        this.targetY = this.y;

        this.df = new DecimalFormat(getPattern(precision, isPercent));
    }

    private String getPattern(int precision, boolean isPercent) {
        if (precision <= 0)
            return isPercent ? "0%" : "0";
        StringBuilder sb = new StringBuilder("0.");
        for (int i = 0; i < precision; i++)
            sb.append("0");
        if (isPercent)
            sb.append("%");
        return sb.toString();
    }

    private float getNormalizedValue() {
        return (value - minValue) / (maxValue - minValue);
    }

    private void setNormalizedValue(float normalized) {
        this.value = minValue + normalized * (maxValue - minValue);
    }

    @Override
    public void update() {
        this.y = MathHelper.cardLerpSnap(this.y, this.targetY);
        this.hb.move(this.x, this.y);
        this.bgHb.move(BG_X, y);

        this.hb.update();
        this.bgHb.update();

        if (this.sliderGrabbed) {
            if (InputHelper.isMouseDown) {
                this.x = MathHelper.fadeLerpSnap(this.x, InputHelper.mX);
                if (this.x < L_X)
                    this.x = L_X;
                else if (this.x > L_X + SLIDE_W)
                    this.x = L_X + SLIDE_W;

                setNormalizedValue((this.x - L_X) / SLIDE_W);
                modifyValue();
            } else {
                this.sliderGrabbed = false;
            }
        } else if (InputHelper.justClickedLeft) {
            if (this.hb.hovered || this.bgHb.hovered)
                this.sliderGrabbed = true;
        }

        if (Settings.isControllerMode && this.bgHb.hovered) {
            if (CInputActionSet.inspectLeft.isJustPressed()) {
                this.x -= 5.0F * Settings.scale;
                if (this.x < L_X)
                    this.x = L_X;
                setNormalizedValue((this.x - L_X) / SLIDE_W);
                modifyValue();
            } else if (CInputActionSet.inspectRight.isJustPressed()) {
                this.x += 5.0F * Settings.scale;
                if (this.x > L_X + SLIDE_W)
                    this.x = L_X + SLIDE_W;
                setNormalizedValue((this.x - L_X) / SLIDE_W);
                modifyValue();
            }
        }
    }

    private void modifyValue() {
        if (onChange != null) {
            float rounded = (float) (Math.round(value * Math.pow(10, precision)) / Math.pow(10, precision));
            onChange.accept(rounded);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);

        sb.draw(ImageMaster.OPTION_SLIDER_BG,
                BG_X - 125.0F, this.y - 12.0F,
                125.0F, 12.0F, 250.0F, 24.0F,
                Settings.scale, Settings.scale,
                0.0F, 0, 0, 250, 24, false, false);

        FontHelper.renderFontCentered(
                sb,
                FontHelper.tipBodyFont,
                df.format(value),
                BG_X + 170.0F * Settings.scale,
                this.y,
                sliderGrabbed ? Settings.GREEN_TEXT_COLOR : Settings.BLUE_TEXT_COLOR);

        sb.draw(ImageMaster.OPTION_SLIDER,
                this.x - 22.0F, this.y - 22.0F,
                22.0F, 22.0F, 44.0F, 44.0F,
                Settings.scale, Settings.scale,
                0.0F, 0, 0, 44, 44, false, false);

        this.hb.render(sb);
        this.bgHb.render(sb);
    }

    @Override
    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float v) {
        this.value = clamp(v, minValue, maxValue);
        this.x = L_X + SLIDE_W * getNormalizedValue();
    }

    public float getY() {
        return y;
    }

    @Override
    public float getHeight() {
        return ImageMaster.OPTION_SLIDER.getHeight() * Settings.scale;
    }
}
