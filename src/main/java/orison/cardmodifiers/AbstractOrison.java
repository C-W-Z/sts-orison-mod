package orison.cardmodifiers;

import static orison.OrisonMod.makeOrisonPath;
import static orison.util.GeneralUtils.removePrefix;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

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
        renderHelper(card, sb, color, toDraw, -100, 50, 1);
    }

    protected void renderHelper(AbstractCard card, SpriteBatch sb, Color color, Texture img,
            float offsetX, float offsetY, float extraScale) {
        sb.setColor(color);
        float drawX = card.current_x + offsetX * Settings.scale;
        float drawY = card.current_y + offsetY * Settings.scale;
        sb.draw(img,
                drawX - img.getWidth() / 2f,
                drawY - img.getHeight() / 2f,
                img.getWidth() / 2f, img.getHeight() / 2f,
                img.getWidth(), img.getHeight(),
                card.drawScale * Settings.scale * extraScale,
                card.drawScale * Settings.scale * extraScale,
                card.angle,
                0, 0,
                img.getWidth(), img.getHeight(),
                false, false);
    }
}
