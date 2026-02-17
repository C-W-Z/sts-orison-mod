package orison.ui.components;

import java.util.function.Supplier;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import orison.core.interfaces.ConfigUIElement;

public class ConfigButton implements ConfigUIElement {

    private Texture icon;

    public static final int PIXEL = 80;
    public static final float WIDTH = 44 * Settings.scale;

    public Hitbox hb;
    public float y;
    public float targetY;

    protected Supplier<Void> onClick;

    public ConfigButton(float cX, float cY, Texture icon) {
        hb = new Hitbox(WIDTH, WIDTH);
        hb.move(cX, cY);
        this.y = targetY = cY;
        this.icon = icon;
    }

    @Override
    public void update() {
        this.y = MathHelper.cardLerpSnap(this.y, this.targetY);
        hb.move(hb.cX, this.y);
        hb.update();
        if (hb.justHovered)
            playHoverSound();
        if (hb.hovered && InputHelper.justClickedLeft)
            hb.clickStarted = true;
        if (hb.clicked) {
            hb.clicked = false;
            onClick.get();
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
            sb.draw(icon,
                    hb.cX - WIDTH / 2, hb.cY - WIDTH / 2,
                    WIDTH / 2, WIDTH / 2, WIDTH, WIDTH,
                    1.2F, 1.2F, 0.0F,
                    0, 0, PIXEL, PIXEL, false, false);
            sb.setColor(Color.GOLD);
            sb.setBlendFunction(770, 1);
            sb.draw(icon,
                    hb.cX - WIDTH / 2, hb.cY - WIDTH / 2,
                    WIDTH / 2, WIDTH / 2, WIDTH, WIDTH,
                    1.2F, 1.2F, 0.0F,
                    0, 0, PIXEL, PIXEL, false, false);
            sb.setBlendFunction(770, 771);
            sb.setColor(Color.WHITE);
        } else {
            sb.draw(icon,
                    hb.cX - WIDTH / 2, hb.cY - WIDTH / 2,
                    WIDTH / 2, WIDTH / 2, WIDTH, WIDTH,
                    1, 1, 0.0F,
                    0, 0, PIXEL, PIXEL, false, false);
        }
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
