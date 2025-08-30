package orison.ui.components;

import static orison.core.OrisonMod.makeID;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import orison.core.abstracts.AbstractOrison;
import orison.core.abstracts.AbstractOrison.UseType;
import orison.core.interfaces.ConfigUIElement;

public class OrisonUseTypeConfigOption implements ConfigUIElement {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID(OrisonUseTypeConfigOption.class.getSimpleName()));

    public static final float ARROW_WIDTH = ImageMaster.CF_LEFT_ARROW.getWidth() * Settings.scale;
    public static final float WIDTH = 325 * Settings.scale;

    private AbstractOrison orison;

    private Hitbox prevHb;
    private Hitbox nextHb;

    private int index = 0;

    private float cX;
    private float cY;

    UseType[] types = UseType.values();

    public OrisonUseTypeConfigOption(float cX, float cY, AbstractOrison orison) {
        this.cX = cX;
        this.cY = cY;
        this.orison = orison;
        for (int i = 0; i < types.length; i++) {
            if (types[i] == orison.getUseType()) {
                this.index = i;
                break;
            }
        }
        this.prevHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        this.nextHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        this.prevHb.move(cX - WIDTH / 2F + ARROW_WIDTH / 2F + 5F * Settings.xScale, cY);
        this.nextHb.move(cX + WIDTH / 2F - ARROW_WIDTH / 2F - 5F * Settings.xScale, cY);
    }

    @Override
    public void update() {
        prevHb.update();
        nextHb.update();

        if (prevHb.clicked) {
            prevHb.clicked = false;
            CardCrawlGame.sound.play("UI_CLICK_1");
            prevOption();
        }

        if (nextHb.clicked) {
            nextHb.clicked = false;
            CardCrawlGame.sound.play("UI_CLICK_1");
            nextOption();
        }

        if (InputHelper.justClickedLeft) {
            if (prevHb.hovered)
                prevHb.clickStarted = true;
            if (nextHb.hovered)
                nextHb.clickStarted = true;
        }
    }

    private void prevOption() {
        index = (index + 1 + types.length) % (types.length);
        orison.saveUseType(types[index]);
    }

    private void nextOption() {
        index = (index + 1) % (types.length);
        orison.saveUseType(types[index]);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);

        FontHelper.renderFontCentered(
                sb,
                FontHelper.cardTitleFont,
                uiStrings.TEXT_DICT.get(types[index].name()),
                cX,
                cY,
                Settings.GOLD_COLOR);

        if (prevHb.hovered)
            sb.setColor(Color.LIGHT_GRAY);
        else
            sb.setColor(Color.WHITE);

        sb.draw(ImageMaster.CF_LEFT_ARROW, prevHb.cX - 24.0F, prevHb.cY - 24.0F, 24.0F, 24.0F, 48.0F, 48.0F,
                Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

        if (nextHb.hovered)
            sb.setColor(Color.LIGHT_GRAY);
        else
            sb.setColor(Color.WHITE);

        sb.draw(ImageMaster.CF_RIGHT_ARROW, nextHb.cX - 24.0F, nextHb.cY - 24.0F, 24.0F, 24.0F, 48.0F,
                48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

        prevHb.render(sb);
        nextHb.render(sb);
    }

    @Override
    public void setTargetY(float targetY) {

    }

    public float getY() {
        return cY;
    }

    @Override
    public float getHeight() {
        return ARROW_WIDTH;
    }
}
