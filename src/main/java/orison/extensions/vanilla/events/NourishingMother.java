package orison.extensions.vanilla.events;

import static orison.core.OrisonMod.makeEventPath;
import static orison.core.OrisonMod.makeID;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import basemod.helpers.CardModifierManager;
import orison.cardmodifiers.ShowOrisonChangeModifier;
import orison.core.abstracts.AbstractOrison;
import orison.extensions.vanilla.orisons.event.Birth;
import orison.utils.GeneralUtils;
import orison.utils.OrisonHelper;

public class NourishingMother extends AbstractImageEvent {

    private static final Logger logger = LogManager.getLogger(NourishingMother.class);

    public static final String ID = makeID(NourishingMother.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String title = eventStrings.NAME;

    private enum CurScreen {
        INTRO, RESULT;
    }

    private CurScreen screen;

    private boolean orisonResult = false;
    private CardGroup cardsToAttachOrison;
    private boolean adv = false;

    private static final int CARD_TO_ATTACH_BIRTH = 1;
    private static final int N_CURSE_MORE = 2;
    private static final int N_CURSE_LESS = 1;

    public NourishingMother() {
        super(title, DESCRIPTIONS[0], makeEventPath(GeneralUtils.removePrefix(ID) + ".png"));
        screen = CurScreen.INTRO;
        this.imageEventText.setDialogOption(String.format(OPTIONS[0], CARD_TO_ATTACH_BIRTH, N_CURSE_MORE));
        this.imageEventText.setDialogOption(String.format(OPTIONS[1], CARD_TO_ATTACH_BIRTH, N_CURSE_LESS));
        this.imageEventText.setDialogOption(String.format(OPTIONS[2]));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.adv = true;
                        cardsToAttachOrison = getCardsToAttachBirth(adv);
                        AbstractDungeon.gridSelectScreen.open(
                                cardsToAttachOrison, 1, "Select a card to attach Adv. Birth",
                                false, false, false, false);
                        this.orisonResult = true;
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3]);
                        this.screen = CurScreen.RESULT;
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.adv = false;
                        cardsToAttachOrison = getCardsToAttachBirth(adv);
                        AbstractDungeon.gridSelectScreen.open(
                                cardsToAttachOrison, 1, "Select a card to attach Birth",
                                false, false, false, false);
                        this.orisonResult = true;
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3]);
                        this.screen = CurScreen.RESULT;
                        break;
                    case 2:
                        this.openMap();
                        logMetricIgnored(title);
                        break;
                    default:
                        logMetricIgnored(title);
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }

    public void update() {
        super.update();
        attachLogic();
        if (this.waitForInput)
            buttonEffect(GenericEventDialog.getSelectedOption());
    }

    private CardGroup getCardsToAttachBirth(boolean adv) {
        CardGroup group = new CardGroup(CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            if (AbstractOrison.canAttachOrison(c))
                group.group.add(c.makeSameInstanceOf());

        for (AbstractCard c : group.group) {
            List<AbstractOrison> oldOrisons = OrisonHelper.getOrisons(c);
            oldOrisons.forEach(o -> logger.info("card " + c.cardID + " has orison: " + o.identifier(c)));

            if (oldOrisons.isEmpty())
                CardModifierManager.addModifier(c, new Birth(adv));
            else
                CardModifierManager.addModifier(c, new ShowOrisonChangeModifier(oldOrisons.get(0), new Birth(adv)));
        }

        return group;
    }

    private void attachLogic() {
        if (this.orisonResult && !AbstractDungeon.isScreenUp
                && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

            AbstractCard cardToAttach = null;
            for (AbstractCard tmpCard : AbstractDungeon.player.masterDeck.group) {
                if (AbstractOrison.canAttachOrison(tmpCard) && tmpCard.uuid.equals(c.uuid)) {
                    cardToAttach = tmpCard;
                    break;
                }
            }

            CardModifierManager.addModifier(cardToAttach, new Birth(adv));
            AbstractDungeon.topLevelEffects.add(
                    new ShowCardBrieflyEffect(cardToAttach.makeStatEquivalentCopy(),
                            Settings.WIDTH / 4F, Settings.HEIGHT / 2F));
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 4F, Settings.HEIGHT / 2F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.orisonResult = false;

            List<String> obtainCurses = new ArrayList<>();
            for (int i = 0; i < (adv ? N_CURSE_MORE : N_CURSE_LESS); i++) {
                AbstractCard curse = CardLibrary.getCurse().makeCopy();
                obtainCurses.add(curse.cardID);
                AbstractDungeon.effectList.add(
                        new ShowCardAndObtainEffect(curse, Settings.WIDTH * 3 / 4F, Settings.HEIGHT / 2F));
            }
            AbstractEvent.logMetricObtainCards(title, adv ? "Bottoms Up" : "Take a Sip", obtainCurses);
        }
    }
}
