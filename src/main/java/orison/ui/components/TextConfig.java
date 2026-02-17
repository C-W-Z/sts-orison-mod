package orison.ui.components;

import static orison.core.OrisonMod.makeUIPath;

import java.util.function.Supplier;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import orison.core.abstracts.AbstractOrison;
import orison.core.interfaces.ConfigUIElement;
import orison.utils.TexLoader;

public class TextConfig implements ConfigUIElement {

    private static final Logger logger = LogManager.getLogger(TextConfig.class);

    public static final Texture BG_LEFT = TexLoader.getTexture(makeUIPath("TextConfig/left.png"));
    public static final Texture BG_MIDDLE = TexLoader.getTexture(makeUIPath("TextConfig/middle.png"));
    public static final Texture BG_RIGHT = TexLoader.getTexture(makeUIPath("TextConfig/right.png"));

    public static final float PADDING_X = 5 * Settings.xScale;
    public static final float PADDING_Y = 5 * Settings.yScale;

    public float x; // 左側x座標
    public float rightX; // 右側x座標

    private String description;
    private ConfigUIElement option;

    private static float text_max_width;
    private static float line_spacing;

    private float textHeight;

    private boolean showBg = true;
    private Supplier<Boolean> showOption = null;

    public TextConfig(float x, float rightX, float y, String label, boolean value, Consumer<Boolean> onChange) {
        this(x, rightX, y, label);
        this.option = new CheckBox(rightX - CheckBox.WIDTH / 2, y, value, onChange);
    }

    public TextConfig(float x, float rightX, float y, String label, float value, Consumer<Float> onChange) {
        this(x, rightX, y, label);
        this.option = new HundredPercentSlider(rightX - CustomRangeSlider.REAL_W, y, value, onChange);
    }

    public TextConfig(float x, float rightX, float y, String label, float value, float min, float max, int precision,
            boolean isPercent, Consumer<Float> onChange) {
        this(x, rightX, y, label);
        this.option = new CustomRangeSlider(
                rightX - CustomRangeSlider.REAL_W, y,
                value, min, max, precision, isPercent, onChange);
    }

    public TextConfig(float x, float rightX, float y, String label, AbstractOrison orison) {
        this(x, rightX, y, label);
        this.option = new OrisonUseTypeConfigOption(rightX - OrisonUseTypeConfigOption.WIDTH / 2F, y, orison);
    }

    public TextConfig(float x, float rightX, float y, String label, String option, int index) {
        this(x, rightX, y, label);
        switch (option) {
            case "SaveConfigButton":
                this.option = new SaveConfigButton(rightX - CheckBox.WIDTH / 2, y, index);
                break;
            case "LoadConfigButton":
                this.option = new LoadConfigButton(rightX - CheckBox.WIDTH / 2, y, index);
                break;
            default:
                break;
        }
    }

    public TextConfig setBg(boolean show) {
        showBg = show;
        return this;
    }

    public TextConfig setShowOption(Supplier<Boolean> showOption) {
        this.showOption = showOption;
        return this;
    }

    /**
     * Don't use this constructor, this is just an encapsulation of the same part of
     * other constructors
     */
    public TextConfig(float x, float rightX, float y, String label) {
        this.x = x;
        this.rightX = rightX;
        this.description = label;
        if (label == null)
            this.description = "[Missing Text]";

        line_spacing = Settings.BIG_TEXT_MODE ? (40.0F * Settings.scale) : (32.0F * Settings.scale);
        text_max_width = Settings.isMobile ? (1170.0F * Settings.scale) : (1050.0F * Settings.scale);

        textHeight = FontHelper.getSmartHeight(FontHelper.charDescFont, label, text_max_width, line_spacing);
    }

    @Override
    public void setTargetY(float targetY) {
        if (option != null)
            option.setTargetY(targetY);
    }

    @Override
    public void update() {
        if (option != null && (showOption == null || showOption.get()))
            option.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        float cY;
        if (option != null)
            cY = option.getY();
        else {
            logger.error("NO Option UI");
            return;
        }
        float width = rightX - x;
        float height = getHeight();
        float y = cY - height / 2;

        if (showBg) {
            sb.setColor(Color.DARK_GRAY);
            float bgH = height + 2 * PADDING_Y;
            sb.draw(BG_LEFT, x - PADDING_X, y - PADDING_Y, bgH, bgH);
            sb.draw(BG_MIDDLE, x - PADDING_X + bgH, y - PADDING_Y, width + 2 * PADDING_X - 2 * bgH, bgH);
            sb.draw(BG_RIGHT, x + width + PADDING_X - bgH, y - PADDING_Y, bgH, bgH);
        }

        sb.setColor(Color.WHITE);
        FontHelper.renderFontLeft(sb,
                FontHelper.charDescFont,
                description,
                x + PADDING_X, // 讓左邊是兩倍的padding
                cY,
                Settings.CREAM_COLOR);

        if (option != null && (showOption == null || showOption.get()))
            option.render(sb);

        if (Settings.isDebug || Settings.isInfo) {
            sb.setColor(Color.RED);
            sb.draw(ImageMaster.DEBUG_HITBOX_IMG,
                    x, y, width, height);
        }
    }

    @Override
    public float getHeight() {
        if (option != null)
            return Math.max(textHeight, option.getHeight());
        return textHeight;
    }

    @Override
    public float getY() {
        if (this.option == null)
            return 0;
        return this.option.getY();
    }
}
