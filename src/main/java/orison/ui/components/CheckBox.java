package orison.ui.components;

import java.util.function.Consumer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import orison.core.interfaces.ConfigUIElement;

public class CheckBox implements ConfigUIElement {

    public static final float WIDTH = 44 * Settings.scale;

    public Hitbox hb;
    public float targetY;

    public boolean selected;
    private Consumer<Boolean> onChange;

    public CheckBox(float cX, float cY, boolean selected, Consumer<Boolean> onChange) {
        hb = new Hitbox(WIDTH, WIDTH);
        hb.move(cX, cY);
        this.selected = selected;
        this.onChange = onChange;
    }

    @Override
    public void update() {
        hb.update();
        hb.move(hb.cX, MathHelper.cardLerpSnap(hb.cY, targetY));
        hb.update();
        if (hb.justHovered)
            playHoverSound();
        if (hb.hovered && InputHelper.justClickedLeft)
            hb.clickStarted = true;
        if (hb.clicked) {
            hb.clicked = false;
            selected = !selected;
            onChange.accept(selected);
            playClickSound();
        }
    }

    private void playHoverSound() {
        CardCrawlGame.sound.playV("UI_HOVER", 0.75F);
    }

    private void playClickSound() {
        CardCrawlGame.sound.playA("UI_CLICK_1", -0.1F);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (hb.hovered) {
            sb.draw(ImageMaster.CHECKBOX,
                    hb.cX - WIDTH / 2, hb.cY - WIDTH / 2,
                    WIDTH / 2, WIDTH / 2, WIDTH, WIDTH,
                    1.2F, 1.2F, 0.0F,
                    0, 0, 64, 64, false, false);
            sb.setColor(Color.GOLD);
            sb.setBlendFunction(770, 1);
            sb.draw(ImageMaster.CHECKBOX,
                    hb.cX - WIDTH / 2, hb.cY - WIDTH / 2,
                    WIDTH / 2, WIDTH / 2, WIDTH, WIDTH,
                    1.2F, 1.2F, 0.0F,
                    0, 0, 64, 64, false, false);
            sb.setBlendFunction(770, 771);
            sb.setColor(Color.WHITE);
        } else {
            sb.draw(ImageMaster.CHECKBOX,
                    hb.cX - WIDTH / 2, hb.cY - WIDTH / 2,
                    WIDTH / 2, WIDTH / 2, WIDTH, WIDTH,
                    1, 1, 0.0F,
                    0, 0, 64, 64, false, false);
        }
        if (this.selected)
            sb.draw(ImageMaster.TICK,
                    hb.cX - WIDTH / 2, hb.cY - WIDTH / 2,
                    WIDTH / 2, WIDTH / 2, WIDTH, WIDTH,
                    1, 1, 0.0F,
                    0, 0, 64, 64, false, false);
        hb.render(sb);
    }

    @Override
    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }

    public float getY() {
        return hb.cY;
    }

    @Override
    public float getHeight() {
        return hb.height;
    }
}
