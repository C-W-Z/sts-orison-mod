package orison.ui.components;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import orison.core.abstracts.AbstractOrison;
import orison.ui.screens.OrisonPopup;

public class OrisonUIElement {

    public static final float SIZE = 150 * Settings.scale;

    public AbstractOrison orison;
    public AbstractOrison orisonNorm;
    public AbstractOrison orisonAdv;

    protected float cX;
    protected float cY;
    protected float drawScale;

    public Hitbox hb;

    public float targetX;
    public float targetY;
    public float targetDrawScale;

    public OrisonUIElement(AbstractOrison orison, float cX, float cY) {
        this.orison = orison;
        this.orisonNorm = orison.newInstance(false);
        this.orisonAdv = orison.newInstance(true);

        this.cX = cX * Settings.scale;
        this.cY = cY * Settings.scale;
        this.drawScale = 1F;

        this.hb = new Hitbox(SIZE * this.drawScale, SIZE * this.drawScale);
        this.hb.move(this.cX, this.cY);

        this.targetX = this.cX;
        this.targetY = this.cY;
        this.targetDrawScale = drawScale;
    }

    protected boolean allowUpgradePreview() {
        return orison.hasAdv;
    }

    public void update() {
        if (allowUpgradePreview() && OrisonPopup.isViewingUpgrade)
            orison = orisonAdv;
        else
            orison = orisonNorm;

        this.cX = MathHelper.cardLerpSnap(this.cX, this.targetX);
        this.cY = MathHelper.cardLerpSnap(this.cY, this.targetY);
        this.hb.resize(SIZE * this.drawScale, SIZE * this.drawScale);
        this.hb.move(this.cX, this.cY);

        if (this.hb.clickStarted && this.hb.hovered) {
            this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale * 0.9F);
            this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale * 0.9F);
        } else {
            this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale);
        }

        updateHoverLogic();

        if (this.hb.hovered && InputHelper.justClickedLeft)
            CardCrawlGame.sound.playA("UI_CLICK_1", -0.1F);
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);

        Texture img = orison.getImageAndColor().getKey();
        sb.draw(img,
                cX - SIZE / 2f,
                cY - SIZE / 2f,
                SIZE / 2F, SIZE / 2F,
                SIZE, SIZE,
                drawScale * Settings.scale,
                drawScale * Settings.scale,
                0, 0, 0,
                img.getWidth(), img.getHeight(),
                false, false);

        this.hb.render(sb);
        // renderHb(sb);
    }

    public void updateHoverLogic() {
        this.hb.update();
        if (this.hb.justHovered)
            CardCrawlGame.sound.playV("UI_HOVER", 0.75F);
        if (this.hb.hovered)
            this.onHover();
        else
            this.onUnhover();
    }

    protected void onHover() {
        this.drawScale = 1.0F;
        this.targetDrawScale = 1.0F;

        ArrayList<PowerTip> tips = new ArrayList<>();
        tips.add(new PowerTip(orison.getTitle(), orison.getDescription()));
        TipHelper.queuePowerTips(InputHelper.mX - 150 * Settings.scale, InputHelper.mY - 100 * Settings.scale, tips);
    }

    protected void onUnhover() {
        this.targetDrawScale = 0.75F;
    }

    void renderHb(SpriteBatch sb) {
        if (this.hb.clickStarted)
            sb.setColor(Color.CHARTREUSE);
        else
            sb.setColor(Color.RED);
        sb.draw(ImageMaster.DEBUG_HITBOX_IMG, this.hb.x, this.hb.y, this.hb.width, this.hb.height);
    }
}
