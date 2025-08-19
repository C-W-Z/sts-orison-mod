package orison.ui;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class ConfigToggleButton {

    public Consumer<Boolean> onChange;

    public String description;
    public boolean selected = false;
    public Hitbox hb;
    private static float offset_x = 0.0F;
    private static float text_max_width;
    private static float line_spacing;
    private static final float OFFSET_Y = 130.0F * Settings.scale;
    public float height;

    private Set<ConfigToggleButton> mutuallyExclusive;

    public ConfigToggleButton(String description, Consumer<Boolean> onChange) {
        if (offset_x == 0.0F) {
            offset_x = (Settings.isMobile
                    ? 240.0F * Settings.xScale
                    : 300.0F * Settings.xScale)
                    + 120.0F * Settings.scale;
            line_spacing = Settings.BIG_TEXT_MODE ? (40.0F * Settings.scale) : (32.0F * Settings.scale);
            text_max_width = Settings.isMobile ? (1170.0F * Settings.scale) : (1050.0F * Settings.scale);
        }
        this.description = description;
        setOnChange(onChange);
        hb = new Hitbox(text_max_width, 70.0F * Settings.scale);
        height = -FontHelper.getSmartHeight(FontHelper.charDescFont, description, text_max_width, line_spacing)
                + 70.0F * Settings.scale;
    }

    public void setOnChange(Consumer<Boolean> onChange) {
        this.onChange = onChange;
    }

    public void update(float y) {
        this.hb.update();
        this.hb.move(offset_x + (text_max_width - 80.0F * Settings.scale) / 2.0F, y + OFFSET_Y);
        if (this.hb.justHovered)
            playHoverSound();
        if (this.hb.hovered && InputHelper.justClickedLeft)
            this.hb.clickStarted = true;
        if (this.hb.clicked) {
            this.hb.clicked = false;
            this.toggle(!this.selected);
            playClickSound();
            if (this.selected)
                disableMutuallyExclusiveMods();
        }
    }

    public void render(SpriteBatch sb) {
        float scale = Settings.isMobile ? (Settings.scale * 1.2F) : Settings.scale;
        if (this.hb.hovered) {
            sb.draw(ImageMaster.CHECKBOX, offset_x - 32.0F, this.hb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F,
                    scale * 1.2F, scale * 1.2F, 0.0F, 0, 0, 64, 64, false, false);
            sb.setColor(Color.GOLD);
            sb.setBlendFunction(770, 1);
            sb.draw(ImageMaster.CHECKBOX, offset_x - 32.0F, this.hb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F,
                    scale * 1.2F, scale * 1.2F, 0.0F, 0, 0, 64, 64, false, false);
            sb.setBlendFunction(770, 771);
            sb.setColor(Color.WHITE);
        } else {
            sb.draw(ImageMaster.CHECKBOX, offset_x - 32.0F, this.hb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, scale,
                    scale, 0.0F, 0, 0, 64, 64, false, false);
        }
        if (this.selected)
            sb.draw(ImageMaster.TICK, offset_x - 32.0F, this.hb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, scale, scale,
                    0.0F, 0, 0, 64, 64, false, false);
        if (this.hb.hovered) {
            FontHelper.renderSmartText(sb, FontHelper.charDescFont, description, offset_x + 46.0F * Settings.scale,
                    this.hb.cY + 12.0F * Settings.scale, text_max_width, line_spacing, Settings.LIGHT_YELLOW_COLOR);
        } else {
            FontHelper.renderSmartText(sb, FontHelper.charDescFont, description, offset_x + 40.0F * Settings.scale,
                    this.hb.cY + 12.0F * Settings.scale, text_max_width, line_spacing, Settings.CREAM_COLOR);
        }
        this.hb.render(sb);
    }

    private void playClickSound() {
        CardCrawlGame.sound.playA("UI_CLICK_1", -0.1F);
    }

    private void playHoverSound() {
        CardCrawlGame.sound.playV("UI_HOVER", 0.75F);
    }

    public void setMutualExclusionPair(ConfigToggleButton otherConfig) {
        setMutualExclusion(otherConfig);
        otherConfig.setMutualExclusion(this);
    }

    private void setMutualExclusion(ConfigToggleButton otherConfig) {
        if (this.mutuallyExclusive == null)
            this.mutuallyExclusive = new HashSet<>();
        this.mutuallyExclusive.add(otherConfig);
    }

    private void disableMutuallyExclusiveMods() {
        if (this.mutuallyExclusive != null)
            for (ConfigToggleButton config : mutuallyExclusive)
                config.toggle(false);
    }

    private void toggle(boolean selected) {
        this.selected = selected;
        this.onChange.accept(this.selected);
    }
}
