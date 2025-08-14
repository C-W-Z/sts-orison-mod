package orison.cardmodifiers;

import static orison.OrisonMod.makeOrisonPath;
import static orison.util.GeneralUtils.removePrefix;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import basemod.ReflectionHacks;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import orison.util.TexLoader;

/*
 * 所有繼承AbstractOrison的class都要：
 * 1. 標上@SaveIgnore
 * 2. 有一個無參數Constructor，用於OrisonLib中的AutoAdd
 */
public abstract class AbstractOrison extends AbstractCardModifier {

    public String id = "";
    public boolean adv = false;
    public boolean disabled = false;
    public boolean hasAdv = true;
    public boolean hasDisabledImg = false;
    protected Texture image = null;
    protected Texture advImage = null;
    protected Texture disabledImage = null;
    protected Texture advDisabledImage = null;

    public AbstractOrison(String id, boolean hasAdv, boolean hasDisabledImg, boolean adv) {
        this.id = id;
        this.hasAdv = hasAdv;
        this.hasDisabledImg = hasDisabledImg;
        if (hasAdv)
            this.adv = adv;
        initializeImages();
    }

    protected void initializeImages() {
        image = TexLoader.getTexture(makeOrisonPath(removePrefix(id) + ".png"));
        if (hasDisabledImg)
            disabledImage = TexLoader.getTexture(makeOrisonPath(removePrefix(id) + "Disabled.png"));
        if (hasAdv) {
            advImage = TexLoader.getTexture(makeOrisonPath("Adv" + removePrefix(id) + ".png"));
            if (hasDisabledImg)
                advDisabledImage = TexLoader.getTexture(makeOrisonPath("Adv" + removePrefix(id) + "Disabled.png"));
        }
    }

    public abstract AbstractOrison newInstance(boolean adv);

    public AbstractOrison newInstance() {
        return newInstance(false);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return newInstance();
    }

    @Override
    public String identifier(AbstractCard card) {
        return id;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        CardModifierManager.modifiers(card).removeIf(m -> m != this && m instanceof AbstractOrison);
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        Texture toDraw = (hasAdv && adv) ? advImage : image;
        Color color = (disabled && !hasDisabledImg) ? Color.GRAY : Color.WHITE;
        if (disabled && hasDisabledImg)
            toDraw = (hasAdv && adv) ? advDisabledImage : disabledImage;
        Vector2 offset = new Vector2(-100, 50);
        onRenderHelper(sb, card, toDraw, offset, 51, 51, color, 1);
    }

    @Override
    public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
        Texture toDraw = (hasAdv && adv) ? advImage : image;
        Color color = (disabled && !hasDisabledImg) ? Color.GRAY : Color.WHITE;
        if (disabled && hasDisabledImg)
            toDraw = (hasAdv && adv) ? advDisabledImage : disabledImage;
        onSCVRenderHelper(card, sb, color, toDraw);
    }

    protected void onSCVRenderHelper(AbstractCard card, SpriteBatch sb, Color color, Texture img) {
        sb.setColor(color);
        Hitbox hb = ReflectionHacks.getPrivate(CardCrawlGame.cardPopup, SingleCardViewPopup.class, "cardHb");
        float drawX = hb.x + 50;
        float drawY = hb.y + hb.height - img.getHeight() - 50;
        sb.draw(img,
                drawX,
                drawY,
                img.getWidth() / 2F, img.getHeight() / 2F,
                img.getWidth(), img.getHeight(),
                Settings.scale,
                Settings.scale,
                card.angle,
                0, 0,
                img.getWidth(), img.getHeight(),
                false, false);
    }

    protected static void onRenderHelper(SpriteBatch sb, AbstractCard card, Texture img, Vector2 offset,
            float width, float height, Color color, float scaleModifier) {
        if (card.angle != 0.0F)
            offset.rotate(card.angle);
        offset.scl(Settings.scale * card.drawScale);
        float cX = card.current_x + offset.x;
        float cY = card.current_y + offset.y;
        sb.setColor(new Color(color.r, color.g, color.b, card.transparency));
        sb.draw(img,
                cX - width / 2f,
                cY - height / 2f,
                width / 2f, height / 2f,
                width, height,
                card.drawScale * Settings.scale * scaleModifier,
                card.drawScale * Settings.scale * scaleModifier,
                card.angle,
                0, 0,
                img.getWidth(), img.getHeight(),
                false, false);
    }
}
