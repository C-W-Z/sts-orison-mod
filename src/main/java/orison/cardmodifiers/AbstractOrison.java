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
 * 2. 有一個無參數Constructor，用於OrisonLib
 * 3. 有一個參數是boolean adv的construcot，用於OrisonLib
 */
public abstract class AbstractOrison extends AbstractCardModifier {

    public String id = "";
    public boolean adv = false;
    public boolean disabled = false;
    public boolean forceDisabled = false;
    public boolean hasAdv = true;
    public boolean canDisable = false;
    protected Texture image = null;
    protected Texture advImage = null;
    protected Texture disabledImage = null;
    protected Texture advDisabledImage = null;

    public AbstractOrison(String id, boolean hasAdv, boolean canDisable, boolean adv) {
        this.id = id;
        this.hasAdv = hasAdv;
        this.canDisable = canDisable;
        if (hasAdv)
            this.adv = adv;
    }

    @Override
    public String identifier(AbstractCard card) {
        return id;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        CardModifierManager.modifiers(card).removeIf(m -> m != this && m instanceof AbstractOrison);
        image = TexLoader.getTexture(makeOrisonPath(removePrefix(id) + ".png"));
        if (disabled)
            disabledImage = TexLoader.getTexture(makeOrisonPath(removePrefix(id) + "Disabled.png"));
        if (hasAdv) {
            advImage = TexLoader.getTexture(makeOrisonPath("Adv" + removePrefix(id) + ".png"));
            if (disabled)
                advDisabledImage = TexLoader.getTexture(makeOrisonPath("Adv" + removePrefix(id) + "Disabled.png"));
        }
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        Texture toDraw = image;
        if (hasAdv && disabled && adv)
            toDraw = advDisabledImage;
        else if (hasAdv && adv)
            toDraw = advImage;
        else if (!adv && disabled)
            toDraw = disabledImage;
        renderHelper(card, sb, Color.WHITE, toDraw, card.current_x - 100 * Settings.scale, card.current_y + 50 * Settings.scale, 1);
    }

    protected void renderHelper(AbstractCard card, SpriteBatch sb, Color color, Texture img, float drawX, float drawY,
            float scale) {
        sb.setColor(color);
        sb.draw(img, drawX - 51, drawY - 51, 51, 51, 102, 102, card.drawScale * Settings.scale * scale,
                card.drawScale * Settings.scale * scale, card.angle, 0, 0, 102, 102, false, false);
    }
}
