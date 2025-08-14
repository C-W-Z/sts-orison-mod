package orison.cardmodifiers;

import static orison.OrisonMod.makeOrisonPath;
import static orison.util.GeneralUtils.removePrefix;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
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
    public boolean shouldApply(AbstractCard card) {
        CardModifierManager.modifiers(card).removeIf(AbstractOrison.class::isInstance);
        return true;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        List<TooltipInfo> tooltipInfos = new ArrayList<>();
        tooltipInfos.add(new TooltipInfo(getTitle(), getDescription()));
        return tooltipInfos;
    }

    public abstract String getTitle();

    public abstract String getDescription();

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        Texture toDraw = (hasAdv && adv) ? advImage : image;
        Color color = (disabled && !hasDisabledImg) ? Color.GRAY : Color.WHITE;
        if (disabled && hasDisabledImg)
            toDraw = (hasAdv && adv) ? advDisabledImage : disabledImage;
        Vector2 offset = new Vector2(-115, 115);
        onRenderHelper(sb, card, toDraw, offset, 80, 80, color, 1);
    }

    @Override
    public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
        Texture toDraw = (hasAdv && adv) ? advImage : image;
        Color color = (disabled && !hasDisabledImg) ? Color.GRAY : Color.WHITE;
        if (disabled && hasDisabledImg)
            toDraw = (hasAdv && adv) ? advDisabledImage : disabledImage;
        Vector2 offset = new Vector2(-230, 230);
        onSCVRenderHelper(card, sb, color, toDraw, offset, 160, 160);
    }

    protected void onSCVRenderHelper(AbstractCard card, SpriteBatch sb, Color color, Texture img,
            Vector2 offset, float width, float height) {
        sb.setColor(color);
        float cX = Settings.WIDTH / 2F + offset.x;
        float cY = Settings.HEIGHT / 2F + offset.y;
        sb.draw(img,
                cX - width / 2f,
                cY - height / 2f,
                width / 2F, height / 2F,
                width, height,
                Settings.scale, Settings.scale,
                card.angle, 0, 0,
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
                card.angle, 0, 0,
                img.getWidth(), img.getHeight(),
                false, false);
    }
}
