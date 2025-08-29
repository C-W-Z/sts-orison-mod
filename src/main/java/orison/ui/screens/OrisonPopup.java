package orison.ui.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

import basemod.ReflectionHacks;
import orison.core.abstracts.AbstractOrison;
import orison.ui.components.OrisonUIElement;

public class OrisonPopup {

    public static final float CENTER_X = Settings.WIDTH / 2F;
    public static final float CENTER_Y = Settings.HEIGHT / 2F;
    public static final float ORISON_CENTER_Y = Settings.HEIGHT * 3F / 4F;
    private static final float CARD_ENERGY_IMG_WIDTH = 26.0F * Settings.scale;

    public static OrisonPopup instance = null;

    public boolean isOpen = false;

    private List<AbstractOrison> group;

    private AbstractOrison orison;

    private AbstractOrison prevOrison;

    private AbstractOrison nextOrison;

    private Hitbox nextHb;

    private Hitbox prevHb;

    private Hitbox hb;

    private float fadeTimer = 0.0F;

    private Color fadeColor = Color.BLACK.cpy();

    private float drawScale;

    public static boolean isViewingUpgrade = false;

    private Hitbox upgradeHb = new Hitbox(250.0F * Settings.scale, 80.0F * Settings.scale);

    public OrisonPopup() {
        this.prevHb = new Hitbox(200.0F * Settings.scale, 70.0F * Settings.scale);
        this.nextHb = new Hitbox(200.0F * Settings.scale, 70.0F * Settings.scale);
    }

    public void open(AbstractOrison orison, List<AbstractOrison> group) {
        CardCrawlGame.isPopupOpen = true;
        this.prevOrison = null;
        this.nextOrison = null;
        this.prevHb = null;
        this.nextHb = null;
        for (int i = 0; i < group.size(); i++) {
            if (group.get(i) == orison) {
                if (i != 0)
                    this.prevOrison = group.get(i - 1);
                if (i != group.size() - 1)
                    this.nextOrison = group.get(i + 1);
                break;
            }
        }
        this.prevHb = new Hitbox(160.0F * Settings.scale, 160.0F * Settings.scale);
        this.nextHb = new Hitbox(160.0F * Settings.scale, 160.0F * Settings.scale);
        this.prevHb.move(Settings.WIDTH / 4F - 128F * Settings.scale, CENTER_Y);
        this.nextHb.move(Settings.WIDTH * 3 / 4F + 128F * Settings.scale, CENTER_Y);
        this.drawScale = 1F;
        this.hb = new Hitbox(OrisonUIElement.SIZE, OrisonUIElement.SIZE);
        this.hb.move(CENTER_X, ORISON_CENTER_Y);
        this.orison = orison;
        this.group = group;
        this.fadeTimer = 0.25F;
        this.fadeColor.a = 0.0F;
        this.upgradeHb.move(CENTER_X, 70.0F * Settings.scale);
        this.isOpen = true;
    }

    public void close() {
        isViewingUpgrade = false;
        InputHelper.justReleasedClickLeft = false;
        CardCrawlGame.isPopupOpen = false;
        isOpen = false;
    }

    public void update() {
        hb.update();
        updateArrows();
        updateInput();
        updateFade();
        if (allowUpgradePreview())
            updateUpgradePreview();
    }

    protected void updateUpgradePreview() {
        upgradeHb.update();
        if (upgradeHb.hovered && InputHelper.justClickedLeft)
            upgradeHb.clickStarted = true;
        if (upgradeHb.clicked || CInputActionSet.proceed.isJustPressed()) {
            CInputActionSet.proceed.unpress();
            upgradeHb.clicked = false;
            isViewingUpgrade = !isViewingUpgrade;
        }
    }

    protected boolean allowUpgradePreview() {
        return orison.hasAdv;
    }

    protected void updateArrows() {
        if (prevOrison != null) {
            prevHb.update();
            if (prevHb.justHovered)
                CardCrawlGame.sound.play("UI_HOVER");
            if (prevHb.clicked || (prevOrison != null && CInputActionSet.pageLeftViewDeck.isJustPressed())) {
                CInputActionSet.pageLeftViewDeck.unpress();
                openPrev();
            }
        }
        if (nextOrison != null) {
            nextHb.update();
            if (nextHb.justHovered)
                CardCrawlGame.sound.play("UI_HOVER");
            if (nextHb.clicked || (nextOrison != null && CInputActionSet.pageRightViewExhaust.isJustPressed())) {
                CInputActionSet.pageRightViewExhaust.unpress();
                openNext();
            }
        }
    }

    protected void updateInput() {
        if (InputHelper.justClickedLeft) {
            if (prevOrison != null && prevHb.hovered) {
                prevHb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
                return;
            }
            if (nextOrison != null && nextHb.hovered) {
                nextHb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
                return;
            }
        }
        if (InputHelper.justClickedLeft) {
            if (!hb.hovered && !upgradeHb.hovered) {
                close();
                InputHelper.justClickedLeft = false;
                FontHelper.ClearSCPFontTextures();
            }
        } else if (InputHelper.pressedEscape || CInputActionSet.cancel.isJustPressed()) {
            CInputActionSet.cancel.unpress();
            InputHelper.pressedEscape = false;
            close();
            FontHelper.ClearSCPFontTextures();
        }
        if (prevOrison != null && InputActionSet.left.isJustPressed()) {
            openPrev();
        } else if (nextOrison != null && InputActionSet.right.isJustPressed()) {
            openNext();
        }
    }

    protected void openPrev() {
        boolean tmp = isViewingUpgrade;
        close();
        open(prevOrison, group);
        isViewingUpgrade = tmp;
        fadeTimer = 0.0F;
        fadeColor.a = 0.9F;
    }

    protected void openNext() {
        boolean tmp = isViewingUpgrade;
        close();
        open(nextOrison, group);
        isViewingUpgrade = tmp;
        fadeTimer = 0.0F;
        fadeColor.a = 0.9F;
    }

    protected void updateFade() {
        fadeTimer -= Gdx.graphics.getDeltaTime();
        if (fadeTimer < 0.0F)
            fadeTimer = 0.0F;
        fadeColor.a = Interpolation.pow2In.apply(0.9F, 0.0F, fadeTimer * 4.0F);
    }

    public void render(SpriteBatch sb) {
        AbstractOrison copy = null;
        if (isViewingUpgrade) {
            copy = orison;
            orison = orison.newInstance(true);
        }
        sb.setColor(fadeColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
        sb.setColor(Color.WHITE);
        renderOrison(sb);
        renderTitle(sb);
        renderDescription(sb);
        renderArrows(sb);
        hb.render(sb);
        if (nextHb != null)
            nextHb.render(sb);
        if (prevHb != null)
            prevHb.render(sb);
        FontHelper.cardTitleFont.getData().setScale(1.0F);
        if (allowUpgradePreview()) {
            renderUpgradeViewToggle(sb);
            if (Settings.isControllerMode)
                sb.draw(CInputActionSet.proceed
                        .getKeyImg(), upgradeHb.cX - 132.0F * Settings.scale - 32.0F,
                        -32.0F + 67.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale,
                        0.0F, 0, 0, 64, 64, false, false);
        }
        if (copy != null)
            orison = copy;
    }

    protected void renderOrison(SpriteBatch sb) {
        sb.setColor(Color.WHITE);

        Texture img = orison.getImageAndColor().getKey();
        sb.draw(img,
                hb.cX - OrisonUIElement.SIZE / 2f,
                hb.cY - OrisonUIElement.SIZE / 2f,
                OrisonUIElement.SIZE / 2F, OrisonUIElement.SIZE / 2F,
                OrisonUIElement.SIZE, OrisonUIElement.SIZE,
                drawScale * Settings.scale,
                drawScale * Settings.scale,
                0, 0, 0,
                img.getWidth(), img.getHeight(),
                false, false);

        hb.render(sb);
    }

    protected void renderTitle(SpriteBatch sb) {
        String title = orison.getTitle();
        FontHelper.renderFontCentered(
                sb,
                FontHelper.SCP_cardTitleFont_small,
                title.substring(0, title.indexOf("[") - 1),
                CENTER_X,
                ORISON_CENTER_Y + OrisonUIElement.SIZE / 2F + 30F,
                Settings.CREAM_COLOR);
    }

    protected void renderDescription(SpriteBatch sb) {
        renderSmartText(
                sb,
                FontHelper.SCP_cardDescFont,
                orison.getDescription(),
                CENTER_X / 2F,
                ORISON_CENTER_Y - OrisonUIElement.SIZE / 2F - 30F,
                Settings.WIDTH / 2F,
                51F * Settings.scale,
                Settings.CREAM_COLOR,
                0.75F);
    }

    protected void renderArrows(SpriteBatch sb) {
        if (prevOrison != null) {
            sb.draw(ImageMaster.POPUP_ARROW, prevHb.cX - 128.0F, prevHb.cY - 128.0F, 128.0F, 128.0F, 256.0F,
                    256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
            if (Settings.isControllerMode)
                sb.draw(CInputActionSet.pageLeftViewDeck
                        .getKeyImg(), prevHb.cX - 32.0F + 0.0F * Settings.scale,
                        prevHb.cY - 32.0F + 100.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale,
                        Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            if (prevHb.hovered) {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.5F));
                sb.draw(ImageMaster.POPUP_ARROW, prevHb.cX - 128.0F, prevHb.cY - 128.0F, 128.0F, 128.0F,
                        256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
                sb.setColor(Color.WHITE);
                sb.setBlendFunction(770, 771);
            }
        }
        if (nextOrison != null) {
            sb.draw(ImageMaster.POPUP_ARROW, nextHb.cX - 128.0F, nextHb.cY - 128.0F, 128.0F, 128.0F, 256.0F,
                    256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, true, false);
            if (Settings.isControllerMode)
                sb.draw(CInputActionSet.pageRightViewExhaust
                        .getKeyImg(), nextHb.cX - 32.0F + 0.0F * Settings.scale,
                        nextHb.cY - 32.0F + 100.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale,
                        Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            if (nextHb.hovered) {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.5F));
                sb.draw(ImageMaster.POPUP_ARROW, nextHb.cX - 128.0F, nextHb.cY - 128.0F, 128.0F, 128.0F,
                        256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, true, false);
                sb.setColor(Color.WHITE);
                sb.setBlendFunction(770, 771);
            }
        }
    }

    protected void renderUpgradeViewToggle(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.CHECKBOX, upgradeHb.cX - 80.0F * Settings.scale - 32.0F, upgradeHb.cY - 32.0F,
                32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);

        Color upgradeToggleColor = upgradeHb.hovered ? Settings.BLUE_TEXT_COLOR : Settings.GOLD_COLOR;
        FontHelper.renderFont(sb, FontHelper.cardTitleFont, "Adv", upgradeHb.cX - 45.0F * Settings.scale,
                upgradeHb.cY + 10.0F * Settings.scale, upgradeToggleColor);

        if (isViewingUpgrade) {
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.TICK, upgradeHb.cX - 80.0F * Settings.scale - 32.0F, upgradeHb.cY - 32.0F,
                    32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }
        upgradeHb.render(sb);
    }

    protected static void renderSmartText(SpriteBatch sb, BitmapFont font, String msg, float x, float y, float lineWidth,
            float lineSpacing, Color baseColor, float scale) {
        BitmapFont.BitmapFontData data = font.getData();
        float prevScale = data.scaleX;
        data.setScale(scale);
        renderSmartText(sb, font, msg, x, y, lineWidth, lineSpacing, baseColor);
        data.setScale(prevScale);
    }

    protected static void renderSmartText(SpriteBatch sb, BitmapFont font, String msg, float x, float y, float lineWidth,
            float lineSpacing, Color baseColor) {
        if (msg == null)
            return;
        if (Settings.lineBreakViaCharacter && font.getData().markupEnabled) {
            renderSmartTextCN(sb, font, msg, x, y, lineWidth, lineSpacing, baseColor);
            return;
        }
        float curWidth = 0.0F;
        float curHeight = 0.0F;
        FontHelper.layout.setText(font, " ");
        float spaceWidth = FontHelper.layout.width;
        for (String word : msg.split(" ")) {
            if (word.equals("NL")) {
                curWidth = 0.0F;
                curHeight -= lineSpacing;
            } else if (word.equals("TAB")) {
                curWidth += spaceWidth * 5.0F;
            } else {
                TextureAtlas.AtlasRegion orb = identifyOrb(word);
                if (orb == null) {
                    Color color = identifyColor(word).cpy();
                    if (!color.equals(Color.WHITE)) {
                        word = word.substring(2, word.length());
                        color.a = baseColor.a;
                        font.setColor(color);
                    } else {
                        font.setColor(baseColor);
                    }
                    FontHelper.layout.setText(font, word);
                    if (curWidth + FontHelper.layout.width > lineWidth) {
                        curHeight -= lineSpacing;
                        font.draw(sb, word, x, y + curHeight);
                        curWidth = FontHelper.layout.width + spaceWidth;
                    } else {
                        font.draw(sb, word, x + curWidth, y + curHeight);
                        curWidth += FontHelper.layout.width + spaceWidth;
                    }
                } else {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, baseColor.a));
                    float fontSize = font.getCapHeight();
                    float scale = fontSize / CARD_ENERGY_IMG_WIDTH;
                    float iconWidth = CARD_ENERGY_IMG_WIDTH * scale;
                    float iconHeight = CARD_ENERGY_IMG_WIDTH * scale;

                    if (curWidth + iconWidth > lineWidth) {
                        curHeight -= lineSpacing;
                        sb.draw(orb,
                                x, y + curHeight - iconHeight,
                                iconWidth, iconHeight);
                        curWidth = iconWidth + spaceWidth;
                    } else {
                        sb.draw(orb,
                                x + curWidth,
                                y + curHeight - iconHeight,
                                iconWidth, iconHeight);
                        curWidth += iconWidth + spaceWidth;
                    }
                }
            }
        }
    }

    protected static void renderSmartTextCN(SpriteBatch sb, BitmapFont font, String msg, float x, float y,
            float widthMax, float lineSpacing, Color c) {
        FontHelper.layout.setText(font, msg, Color.WHITE, 0.0F, -1, false);
        float currentLine = 0;
        float curWidth = 0.0F;
        for (String word : msg.split(" ")) {
            if (word.length() == 0)
                continue;
            if (word.equals("NL")) {
                curWidth = 0.0F;
                currentLine++;
            } else if (word.equals("TAB")) {
                FontHelper.layout.setText(font, word);
                curWidth += FontHelper.layout.width;
            } else if (word.charAt(0) == '[') {
                TextureAtlas.AtlasRegion orb = identifyOrb(word);
                if (orb != null) {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, c.a));

                    float fontSize = font.getCapHeight();
                    float scale = fontSize / CARD_ENERGY_IMG_WIDTH;
                    float iconWidth = CARD_ENERGY_IMG_WIDTH * scale;
                    float iconHeight = CARD_ENERGY_IMG_WIDTH * scale;

                    if (iconWidth <= widthMax * 2.0F)
                        if (curWidth + iconWidth > widthMax) {
                            sb.draw(orb,
                                    x,
                                    y - currentLine * lineSpacing - iconHeight,
                                    iconWidth, iconHeight);
                        } else {
                            sb.draw(orb,
                                    x + curWidth,
                                    y - currentLine * lineSpacing - iconHeight,
                                    iconWidth, iconHeight);
                        }
                    curWidth += iconWidth;
                    if (curWidth > widthMax) {
                        curWidth = iconWidth;
                        currentLine++;
                    }
                }
            } else if (word.charAt(0) == '#') {
                FontHelper.layout.setText(font, word.substring(2));
                switch (word.charAt(1)) {
                    case 'r':
                        word = "[#ff6563]" + word.substring(2) + "[]";
                        break;
                    case 'g':
                        word = "[#7fff00]" + word.substring(2) + "[]";
                        break;
                    case 'b':
                        word = "[#87ceeb]" + word.substring(2) + "[]";
                        break;
                    case 'y':
                        word = "[#efc851]" + word.substring(2) + "[]";
                        break;
                    case 'p':
                        word = "[#0e82ee]" + word.substring(2) + "[]";
                        break;
                }
                curWidth += FontHelper.layout.width;
                if (curWidth > widthMax) {
                    curWidth = 0.0F;
                    currentLine++;
                    font.draw(sb, word, x + curWidth, y - lineSpacing * currentLine);
                    curWidth = FontHelper.layout.width;
                } else {
                    font.draw(sb, word, x + curWidth - FontHelper.layout.width,
                            y - lineSpacing * currentLine);
                }
            } else {
                font.setColor(c);
                for (int i = 0; i < word.length(); i++) {
                    String j = Character.toString(word.charAt(i));
                    FontHelper.layout.setText(font, j);
                    curWidth += FontHelper.layout.width;
                    if (curWidth > widthMax && !j.equals(LocalizedStrings.PERIOD)) {
                        curWidth = FontHelper.layout.width;
                        currentLine++;
                    }
                    font.draw((Batch) sb, j, x + curWidth - FontHelper.layout.width, y - lineSpacing * currentLine);
                }
            }
        }
    }

    protected static TextureAtlas.AtlasRegion identifyOrb(String word) {
        return ReflectionHacks.privateStaticMethod(FontHelper.class, "identifyOrb", String.class)
                .invoke(new Object[] { word });
    }

    protected static Color identifyColor(String word) {
        return ReflectionHacks.privateStaticMethod(FontHelper.class, "identifyColor", String.class)
                .invoke(new Object[] { word });
    }
}
