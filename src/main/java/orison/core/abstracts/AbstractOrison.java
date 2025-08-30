package orison.core.abstracts;

import static orison.core.OrisonMod.makeID;
import static orison.core.OrisonMod.makeOrisonPath;
import static orison.utils.GeneralUtils.removePrefix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

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

    public static final UIStrings uiStrings = CardCrawlGame.languagePack
            .getUIString(makeID(AbstractOrison.class.getSimpleName()));

    public static final float DEFAULT_RARITY = 100F;

    public enum UseType {
        INFINITE,
        FINITE_TURN,
        FINITE_BATTLE,
    }

    public static final Map<String, Float> id2Rarity = new HashMap<>();

    public static final Map<String, UseType> id2UseType = new HashMap<>();
    public static final Map<String, Integer> id2UseLimit = new HashMap<>();

    public static final Map<String, UseType> id2AdvUseType = new HashMap<>();
    public static final Map<String, Integer> id2AdvUseLimit = new HashMap<>();

    public String id = "";
    public boolean adv = false;
    public boolean hasAdv = true;
    public boolean hasDisabledImg = false;
    protected Texture image = null;
    protected Texture advImage = null;
    protected Texture disabledImage = null;
    protected Texture advDisabledImage = null;

    protected int usesThisTurn = 0;
    protected int usesThisBattle = 0;

    public AbstractOrison(String id, boolean hasAdv, boolean hasDisabledImg, boolean adv) {
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

    public boolean isDisabled() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(AprilTribute.ID) && usesThisTurn >= 1)
            return true;
        switch (getUseType()) {
            case INFINITE:
                return getUseLimit() <= 0;
            case FINITE_TURN:
                return usesThisTurn >= getUseLimit();
            case FINITE_BATTLE:
                return usesThisBattle >= getUseLimit();
            default:
                return false;
        }
    }

    public void loadConfigs() {
        Float rarity = OrisonConfig.OrisonRarity.load(id);
        if (rarity != null) {
            id2Rarity.put(id, rarity);
        }

        List<Integer> list = OrisonConfig.OrisonValues.load(id, adv);
        if (list != null && !list.isEmpty() && list.size() == getValueList().size())
            for (int i = 0; i < list.size(); i++)
                getValueList().set(i, list.get(i));

        UseType type = OrisonConfig.OrisonUseType.load(id, adv);
        if (type != null) {
            Map<String, UseType> map = adv ? id2UseType : id2AdvUseType;
            map.put(id, type);
        }

        Integer uses = OrisonConfig.OrisonUseLimit.load(id, adv);
        if (uses != null) {
            Map<String, Integer> map = adv ? id2UseLimit : id2AdvUseLimit;
            map.put(id, uses);
        }
    }

    protected float getDefaultRarity() {
        return 1.0F;
    }

    public float getRarity() {
        return id2Rarity.getOrDefault(id, getDefaultRarity());
    }

    public void saveRarity(float rarity) {
        id2Rarity.put(id, rarity);
        OrisonConfig.OrisonRarity.save(id, rarity);
    }

    public boolean canSetUseType() {
        return true;
    }

    protected UseType getDefaultUseType() {
        return UseType.INFINITE;
    }

    public UseType getUseType() {
        Map<String, UseType> map = adv ? id2UseType : id2AdvUseType;
        return map.getOrDefault(id, getDefaultUseType());
    }

    public void saveUseType(UseType newType) {
        Map<String, UseType> map = adv ? id2UseType : id2AdvUseType;
        map.put(id, newType);
        OrisonConfig.OrisonUseType.save(id, adv, newType);
    }

    public boolean canSetUseLimit() {
        return true;
    }

    protected int getDefaultUseLimit() {
        return 1;
    }

    public int getUseLimit() {
        Map<String, Integer> map = adv ? id2UseLimit : id2AdvUseLimit;
        return map.getOrDefault(id, getDefaultUseLimit());
    }

    public void saveUseLimit(int newUses) {
        Map<String, Integer> map = adv ? id2UseLimit : id2AdvUseLimit;
        map.put(id, newUses);
        OrisonConfig.OrisonUseLimit.save(id, adv, newUses);
    }

    protected abstract List<Integer> getValueList();

    public int getValue(int index) {
        return getValueList().get(index);
    }

    public void saveValue(int index, int newVal) {
        List<Integer> list = getValueList();
        list.set(index, newVal);
        OrisonConfig.OrisonValues.save(id, adv, list);
    }

    public int getModifiedValue(int index) {
        int base = getValue(index);
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(AprilTribute.ID))
            base *= 2;
        return base;
    }

    @Override
    public boolean onBattleStart(AbstractCard card) {
        usesThisTurn = 0;
        usesThisBattle = 0;
        return false;
    }

    @Override
    public void atStartOfTurn(AbstractCard card, CardGroup group) {
        usesThisTurn = 0;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        logger.info("onUse: id: " + id + ", type: " + getUseType().name() + ", useLimit: " + getUseLimit());
        if (isDisabled())
            return;
        takeEffectOnUse(card, target, action);
        usesThisTurn++;
        usesThisBattle++;
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
        if (!OrisonConfig.Orison.CAN_ATTACH_ON_CURSE
                && (card.type == CardType.CURSE || card.rarity == CardRarity.CURSE))
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
        tooltipInfos.add(new TooltipInfo(getTitle(), getTotalDescription()));
        return tooltipInfos;
    }

    public String getTotalDescription() {
        if (!canSetUseType() && getUseType() == UseType.INFINITE)
            return getDescription();

        String desc = getDescription();
        int useLimit = getUseLimit();
        String postfix = "";
        switch (getUseType()) {
            case INFINITE:
                if (useLimit > 0)
                    postfix = uiStrings.TEXT_DICT.get(UseType.INFINITE.name());
                else
                    postfix = uiStrings.TEXT_DICT.get("DISABLED");
                break;
            case FINITE_TURN:
                postfix = String.format(
                        uiStrings.TEXT_DICT.get(UseType.FINITE_TURN.name()),
                        useLimit, useLimit - usesThisTurn);
                break;
            case FINITE_BATTLE:
                postfix = String.format(
                        uiStrings.TEXT_DICT.get(UseType.FINITE_BATTLE.name()),
                        useLimit, useLimit - usesThisTurn);
                break;
            default:
                break;
        }
        return desc + postfix;
    }

    public abstract String getTitle();

    protected abstract String getDescription();

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
        boolean disabled = isDisabled();
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
