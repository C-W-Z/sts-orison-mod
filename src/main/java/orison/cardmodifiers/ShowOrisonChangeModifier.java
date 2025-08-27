package orison.cardmodifiers;

import static orison.core.OrisonMod.makeID;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.AbstractCardModifier.SaveIgnore;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import orison.core.abstracts.AbstractOrison;

@SaveIgnore
public class ShowOrisonChangeModifier extends AbstractCardModifier {

    public static final String ID = makeID(ShowOrisonChangeModifier.class.getSimpleName());

    public AbstractOrison oldOrison;
    public AbstractOrison newOrison;

    public ShowOrisonChangeModifier(AbstractOrison oldOrison, AbstractOrison newOrison) {
        this.oldOrison = oldOrison;
        this.newOrison = newOrison;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        AbstractOrison.removeAllOrisons(card);
        CardModifierManager.modifiers(card).removeIf(ShowOrisonChangeModifier.class::isInstance);
        return true;
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        List<TooltipInfo> ret = new ArrayList<>();
        ret.addAll(oldOrison.additionalTooltips(card));
        ret.addAll(newOrison.additionalTooltips(card));
        return ret;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ShowOrisonChangeModifier(oldOrison, newOrison);
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        Texture oldImg = oldOrison.getImageAndColor().getKey();
        Vector2 oldOffset = new Vector2(-115, 115);
        AbstractOrison.onRenderHelper(sb, card, oldImg, oldOffset, 80, 80, Color.GRAY);

        Vector2 arrowOffset = new Vector2(-115 + 80 / 2 + 48 / 2, 115);
        AbstractOrison.onRenderHelper(sb, card, ImageMaster.CF_RIGHT_ARROW, arrowOffset, 48, 48, Color.WHITE);

        Texture newImg = newOrison.getImageAndColor().getKey();
        Vector2 newOffset = new Vector2(-115 + 80 + 48, 115);
        AbstractOrison.onRenderHelper(sb, card, newImg, newOffset, 80, 80, Color.WHITE);
    }

    @Override
    public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
        Texture oldImg = oldOrison.getImageAndColor().getKey();
        Vector2 oldOffset = new Vector2(-230, 230);
        AbstractOrison.onSCVRenderHelper(sb, card, oldImg, oldOffset, 160, 160, Color.GRAY);

        Vector2 arrowOffset = new Vector2(-230 + 160 / 2 + 96 / 2, 230);
        AbstractOrison.onSCVRenderHelper(sb, card, ImageMaster.CF_RIGHT_ARROW, arrowOffset, 96, 96, Color.WHITE);

        Texture newImg = newOrison.getImageAndColor().getKey();
        Vector2 newOffset = new Vector2(-230 + 160 + 96, 230);
        AbstractOrison.onSCVRenderHelper(sb, card, newImg, newOffset, 160, 160, Color.WHITE);
    }
}
