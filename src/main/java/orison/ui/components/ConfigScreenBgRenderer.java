package orison.ui.components;

import static orison.core.OrisonMod.makeUIPath;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import orison.core.configs.OrisonConfig;
import orison.utils.TexLoader;

public class ConfigScreenBgRenderer {

    public static final int MAX_BG_INDEX = 6;
    public static final float BG_CHOICE_ARROW_GAP_X = 50 * Settings.scale;
    public static final float BG_CHOICE_CX = 150 * Settings.scale;
    public static final float BG_CHOICE_CY = Settings.HEIGHT - 100 * Settings.scale;
    private List<Texture> bgTextures;
    private int currentBgIndex = 0;
    private Texture bg;
    private Hitbox prevBgHb;
    private Hitbox nextBgHb;

    public ConfigScreenBgRenderer() {
        bgTextures = new ArrayList<>();
        for (int i = 0; i <= MAX_BG_INDEX; i++)
            bgTextures.add(null);
        currentBgIndex = OrisonConfig.Preference.CONFIG_SCREEN_BG;
        if (currentBgIndex != 0) {
            bg = TexLoader.getTexture(makeUIPath("OrisonConfigScreen/bg/" + currentBgIndex + ".png"));
            bgTextures.set(currentBgIndex, bg);
        } else {
            bg = null;
        }
        prevBgHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        nextBgHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        prevBgHb.move(BG_CHOICE_CX - BG_CHOICE_ARROW_GAP_X, BG_CHOICE_CY);
        nextBgHb.move(BG_CHOICE_CX + BG_CHOICE_ARROW_GAP_X, BG_CHOICE_CY);
    }

    public void close() {
        for (int i = 0; i < bgTextures.size(); i++) {
            if (i != currentBgIndex && bgTextures.get(i) != null) {
                bgTextures.get(i).dispose();
                bgTextures.set(i, null);
            }
        }
    }

    public void update() {
        updateBackgroundChoice();
    }

    public void render(SpriteBatch sb) {
        renderBackground(sb);
        renderBackgroundChoice(sb);
    }

    private void prevBackground() {
        // currentBgIndex = (currentBgIndex-1 + MAX_BG_INDEX+1) % (MAX_BG_INDEX+1);
        currentBgIndex = (currentBgIndex + MAX_BG_INDEX) % (MAX_BG_INDEX + 1);
        bg = bgTextures.get(currentBgIndex);
        if (bg == null && currentBgIndex != 0) {
            bg = TexLoader.getTexture(makeUIPath("OrisonConfigScreen/bg/" + currentBgIndex + ".png"));
            bgTextures.set(currentBgIndex, bg);
        }
    }

    private void nextBackground() {
        currentBgIndex = (currentBgIndex + 1) % (MAX_BG_INDEX + 1);
        bg = bgTextures.get(currentBgIndex);
        if (bg == null && currentBgIndex != 0) {
            bg = TexLoader.getTexture(makeUIPath("OrisonConfigScreen/bg/" + currentBgIndex + ".png"));
            bgTextures.set(currentBgIndex, bg);
        }
    }

    private void updateBackgroundChoice() {
        prevBgHb.update();
        nextBgHb.update();

        if (prevBgHb.clicked) {
            prevBgHb.clicked = false;
            CardCrawlGame.sound.play("UI_CLICK_1");
            prevBackground();
            OrisonConfig.Preference.save(OrisonConfig.Preference.ID_CONFIG_SCREEN_BG, currentBgIndex);
        }

        if (nextBgHb.clicked) {
            nextBgHb.clicked = false;
            CardCrawlGame.sound.play("UI_CLICK_1");
            nextBackground();
            OrisonConfig.Preference.save(OrisonConfig.Preference.ID_CONFIG_SCREEN_BG, currentBgIndex);
        }

        if (InputHelper.justClickedLeft) {
            if (prevBgHb.hovered)
                prevBgHb.clickStarted = true;
            if (nextBgHb.hovered)
                nextBgHb.clickStarted = true;
        }
    }

    private void renderBackgroundChoice(SpriteBatch sb) {
        sb.setColor(Color.WHITE);

        FontHelper.renderFontCentered(
                sb,
                FontHelper.cardTitleFont,
                "Background",
                BG_CHOICE_CX,
                BG_CHOICE_CY + 50,
                Settings.GOLD_COLOR,
                1.25F);

        FontHelper.renderFontCentered(
                sb,
                FontHelper.cardTitleFont,
                String.valueOf(currentBgIndex),
                BG_CHOICE_CX,
                BG_CHOICE_CY,
                Settings.GOLD_COLOR);

        if (prevBgHb.hovered)
            sb.setColor(Color.LIGHT_GRAY);
        else
            sb.setColor(Color.WHITE);

        sb.draw(ImageMaster.CF_LEFT_ARROW, prevBgHb.cX - 24.0F, prevBgHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F,
                Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

        if (nextBgHb.hovered)
            sb.setColor(Color.LIGHT_GRAY);
        else
            sb.setColor(Color.WHITE);

        sb.draw(ImageMaster.CF_RIGHT_ARROW, nextBgHb.cX - 24.0F, nextBgHb.cY - 24.0F, 24.0F, 24.0F, 48.0F,
                48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

        prevBgHb.render(sb);
        nextBgHb.render(sb);
    }

    private void renderBackground(SpriteBatch sb) {
        if (bg == null)
            return;
        sb.setColor(Color.WHITE);

        float screenW = Settings.WIDTH;
        float screenH = Settings.HEIGHT;

        float imgW = bg.getWidth();
        float imgH = bg.getHeight();

        float scaleX = screenW / imgW;
        float scaleY = screenH / imgH;

        float coverScale = Math.max(scaleX, scaleY);

        float scaledW = imgW * coverScale;
        float scaledH = imgH * coverScale;

        // 中心對齊
        float drawX = (screenW - scaledW) / 2f;
        float drawY = (screenH - scaledH) / 2f;

        sb.draw(bg, drawX, drawY, scaledW, scaledH);
    }
}
