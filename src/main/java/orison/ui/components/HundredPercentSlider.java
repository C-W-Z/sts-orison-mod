package orison.ui.components;

import static orison.utils.GeneralUtils.clamp;

import java.text.DecimalFormat;
import java.util.function.Consumer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import orison.core.interfaces.ConfigUIElement;

public class HundredPercentSlider extends CustomRangeSlider {

    public HundredPercentSlider(float x, float y, float initialValue, Consumer<Float> onChange) {
        super(x, y, initialValue, 0, 1, 0, true, onChange);
    }

    // public static final float SLIDE_W = 230.0F * Settings.xScale;
    // private float BG_X = 1350.0F * Settings.xScale;
    // private float L_X = 1235.0F * Settings.xScale;

    // private float x;
    // private float y;
    // private float volume;

    // public Hitbox hb;
    // public Hitbox bgHb;

    // private boolean sliderGrabbed = false;

    // private static DecimalFormat df = new DecimalFormat("0%");

    // public Consumer<Float> onChange;

    // private float targetY;

    // public HundredPercentSlider(float x, float y, float volume, Consumer<Float> onChange) {
    //     L_X = x;
    //     BG_X = L_X + 115 * Settings.xScale;
    //     this.y = y;
    //     this.volume = clamp(volume, 0F, 1F);
    //     this.hb = new Hitbox(42.0F * Settings.scale, 38.0F * Settings.scale);
    //     this.bgHb = new Hitbox(300.0F * Settings.scale, 38.0F * Settings.scale);
    //     this.bgHb.move(BG_X, y);
    //     this.x = L_X + SLIDE_W * this.volume;
    //     this.onChange = onChange;
    //     this.targetY = this.y;
    // }

    // public void update() {

    //     this.y = MathHelper.cardLerpSnap(this.y, this.targetY);
    //     this.hb.move(this.x, this.y);
    //     this.bgHb.move(BG_X, y);

    //     this.hb.update();
    //     this.bgHb.update();

    //     if (this.sliderGrabbed) {
    //         if (InputHelper.isMouseDown) {
    //             this.x = MathHelper.fadeLerpSnap(this.x, InputHelper.mX);
    //             if (this.x < L_X)
    //                 this.x = L_X;
    //             else if (this.x > L_X + SLIDE_W)
    //                 this.x = L_X + SLIDE_W;
    //             this.volume = (this.x - L_X) / SLIDE_W;
    //             modifyVolume();
    //         } else {
    //             this.sliderGrabbed = false;
    //         }
    //     } else if (InputHelper.justClickedLeft) {
    //         if (this.hb.hovered)
    //             this.sliderGrabbed = true;
    //         else if (this.bgHb.hovered)
    //             this.sliderGrabbed = true;
    //     }

    //     if (Settings.isControllerMode && this.bgHb.hovered) {
    //         if (CInputActionSet.inspectLeft.isJustPressed()) {
    //             this.x -= 5.0F * Settings.scale;
    //             if (this.x < L_X)
    //                 this.x = L_X;
    //             this.volume = (this.x - L_X) / SLIDE_W;
    //             modifyVolume();
    //         } else if (CInputActionSet.inspectRight.isJustPressed()) {
    //             this.x += 5.0F * Settings.scale;
    //             if (this.x > L_X + SLIDE_W)
    //                 this.x = L_X + SLIDE_W;
    //             this.volume = (this.x - L_X) / SLIDE_W;
    //             modifyVolume();
    //         }
    //     }
    // }

    // private void modifyVolume() {
    //     if (onChange != null)
    //         onChange.accept(MathUtils.round(this.volume * 100F) / 100F);
    // }

    // public void render(SpriteBatch sb) {
    //     sb.setColor(Color.WHITE);

    //     if (Settings.isControllerMode && this.bgHb.hovered)
    //         sb.draw(ImageMaster.CONTROLLER_RS,
    //                 this.bgHb.cX + 195.0F * Settings.scale,
    //                 this.bgHb.cY - 46.0F * Settings.scale,
    //                 32.0F, 32.0F, 64.0F, 64.0F,
    //                 Settings.scale, Settings.scale,
    //                 0.0F, 0, 0, 64, 64, false, false);

    //     sb.draw(ImageMaster.OPTION_SLIDER_BG,
    //             BG_X - 125.0F, this.y - 12.0F,
    //             125.0F, 12.0F, 250.0F, 24.0F,
    //             Settings.scale, Settings.scale,
    //             0.0F, 0, 0, 250, 24, false, false);

    //     if (this.sliderGrabbed) {

    //         FontHelper.renderFontCentered(sb,
    //                 FontHelper.tipBodyFont,
    //                 df.format(this.volume),
    //                 BG_X + 170.0F * Settings.scale,
    //                 this.y,
    //                 Settings.GREEN_TEXT_COLOR);

    //     } else {

    //         FontHelper.renderFontCentered(sb,
    //                 FontHelper.tipBodyFont,
    //                 df.format(this.volume),
    //                 BG_X + 170.0F * Settings.scale,
    //                 this.y,
    //                 Settings.BLUE_TEXT_COLOR);

    //     }

    //     sb.draw(ImageMaster.OPTION_SLIDER,
    //             this.x - 22.0F, this.y - 22.0F,
    //             22.0F, 22.0F, 44.0F, 44.0F,
    //             Settings.scale, Settings.scale,
    //             0.0F, 0, 0, 44, 44, false, false);

    //     this.hb.render(sb);
    //     this.bgHb.render(sb);
    // }

    // @Override
    // public void setTargetY(float targetY) {
    //     this.targetY = targetY;
    // }

    // public float getY() {
    //     return y;
    // }

    // @Override
    // public float getHeight() {
    //     return ImageMaster.OPTION_SLIDER.getHeight() * Settings.scale;
    // }
}
