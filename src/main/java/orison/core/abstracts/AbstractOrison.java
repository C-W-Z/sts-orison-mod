package orison.core.abstracts;

import static orison.core.OrisonMod.makeOrisonPath;
import static orison.utils.GeneralUtils.removePrefix;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.Pair;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import orison.core.configs.OrisonConfig;
import orison.core.interfaces.AtStartOfTurnModifier;
import orison.core.relics.AprilTribute;
import orison.core.relics.DeusExMachina;
import orison.utils.TexLoader;

/*
 * 所有繼承AbstractOrison的class都要：
 * 1. 標上@SaveIgnore
 * 2. 有一個無參數Constructor，用於OrisonLib中的AutoAdd
 */
public abstract class AbstractOrison extends AbstractCardModifier implements AtStartOfTurnModifier {

    private static final Logger logger = LogManager.getLogger(AbstractOrison.class);

    public static final float DEFAULT_RARITY = 100F;

    public String id = "";
    public boolean adv = false;
    public boolean disabled = false;
    public boolean hasAdv = true;
    public boolean hasDisabledImg = false;
    protected Texture image = null;
    protected Texture advImage = null;
    protected Texture disabledImage = null;
    protected Texture advDisabledImage = null;
    public float rarity = DEFAULT_RARITY;

    protected List<Integer> values;
    protected List<Integer> advValues;

    public AbstractOrison(String id, boolean hasAdv, boolean hasDisabledImg, boolean adv) {
        this(id, DEFAULT_RARITY, hasAdv, hasDisabledImg, adv);
    }

    public AbstractOrison(String id, float rarity, boolean hasAdv, boolean hasDisabledImg, boolean adv) {
        this.id = id;
        this.hasAdv = hasAdv;
        this.hasDisabledImg = hasDisabledImg;
        if (hasAdv) {
            this.adv = adv;
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(DeusExMachina.ID)) {
                logger.info("relic DeusExMachina take effect");
                this.adv = true;
            }
        }
        this.rarity = rarity;
        this.values = new ArrayList<>();
        this.advValues = new ArrayList<>();
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

    public int getModifiedValue(int index) {
        return getModifiedValue(index, this.adv);
    }

    public int getModifiedValue(int index, boolean adv) {
        try {
            List<Integer> list = adv ? advValues : values;
            int base = list.get(index);
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(AprilTribute.ID))
                base *= 2;
            return base;
        } catch (Exception e) {
            logger.error("Try to getModifiedValue() with out-of-range index with orison: " + id);
            e.printStackTrace();
            return 0;
        }
    }

    public int getValue(int index) {
        return getValue(index, this.adv);
    }

    public int getValue(int index, boolean adv) {
        try {
            List<Integer> list = adv ? advValues : values;
            return list.get(index);
        } catch (Exception e) {
            logger.error("Try to getValue() with out-of-range index with orison: " + id);
            e.printStackTrace();
            return 0;
        }
    }

    public void setValue(int index, int newVal) {
        setValue(index, newVal, this.adv);
    }

    public void setValue(int index, int newVal, boolean adv) {
        try {
            List<Integer> list = adv ? advValues : values;
            list.set(index, newVal);
        } catch (Exception e) {
            logger.error("Try to setValue() with out-of-range index with orison: " + id);
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void atStartOfTurn(AbstractCard card, CardGroup group) {
        disabled = false;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (disabled)
            return;
        takeEffectOnUse(card, target, action);
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(AprilTribute.ID))
            disabled = true;
    }

    protected abstract void takeEffectOnUse(AbstractCard card, AbstractCreature target, UseCardAction action);

    public abstract AbstractOrison newInstance(boolean adv);

    public AbstractOrison newInstance() {
        return newInstance(false);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return newInstance(adv);
    }

    @Override
    public String identifier(AbstractCard card) {
        return id;
    }

    public static boolean canAttachOrison(AbstractCard card) {
        if (!OrisonConfig.Orison.CAN_ATTACH_ON_UNPLAYABLE_CARD && card.cost < -1)
            return false;
        if (!OrisonConfig.Orison.CAN_ATTACH_ON_STATUS && card.type == CardType.STATUS)
            return false;
        if (!OrisonConfig.Orison.CAN_ATTACH_ON_CURSE && (card.type == CardType.CURSE || card.rarity == CardRarity.CURSE))
            return false;
        if (!OrisonConfig.Orison.CAN_ATTACH_ON_COLORLESS && card.color == CardColor.COLORLESS)
            return false;
        return true;
    }

    public static void removeAllOrisons(AbstractCard card) {
        ArrayList<AbstractCardModifier> removed = new ArrayList<>();
        CardModifierManager.modifiers(card).removeIf(m -> {
            if (m instanceof AbstractOrison) {
                removed.add(m);
                return true;
            }
            return false;
        });
        removed.forEach(m -> m.onRemove(card));
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (!canAttachOrison(card))
            return false;
        removeAllOrisons(card);
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
        if (card.isFlipped || card.isLocked)
            return;
        Pair<Texture, Color> imgAndColor = getImageAndColor();
        Vector2 offset = new Vector2(-115, 115);
        onRenderHelper(sb, card, imgAndColor.getKey(), offset, 80, 80, imgAndColor.getValue());
    }

    @Override
    public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
        if (card.isFlipped || card.isLocked)
            return;
        Pair<Texture, Color> imgAndColor = getImageAndColor();
        Vector2 offset = new Vector2(-230, 230);
        onSCVRenderHelper(sb, card, imgAndColor.getKey(), offset, 160, 160, imgAndColor.getValue());
    }

    public Pair<Texture, Color> getImageAndColor() {
        Texture toDraw = (hasAdv && adv) ? advImage : image;
        Color color = (disabled && !hasDisabledImg) ? Color.GRAY : Color.WHITE;
        if (disabled && hasDisabledImg)
            toDraw = (hasAdv && adv) ? advDisabledImage : disabledImage;
        return new Pair<>(toDraw, color);
    }

    public static void onSCVRenderHelper(SpriteBatch sb, AbstractCard card, Texture img,
            Vector2 offset, float width, float height, Color color) {
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

    public static void onRenderHelper(SpriteBatch sb, AbstractCard card, Texture img, Vector2 offset,
            float width, float height, Color color) {
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
                card.drawScale * Settings.scale,
                card.drawScale * Settings.scale,
                card.angle, 0, 0,
                img.getWidth(), img.getHeight(),
                false, false);
    }
}
