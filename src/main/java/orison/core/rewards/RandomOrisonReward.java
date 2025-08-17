package orison.core.rewards;

import static orison.core.OrisonMod.makeID;
import static orison.core.OrisonMod.makeRewardPath;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import basemod.Pair;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import orison.core.abstracts.AbstractOrison;
import orison.core.abstracts.AbstractOrisonReward;
import orison.core.libs.OrisonLib;
import orison.core.patches.RewardTypePatch;
import orison.utils.TexLoader;

public class RandomOrisonReward extends AbstractOrisonReward {

    private static final Logger logger = LogManager.getLogger(RandomOrisonReward.class);

    public static final RewardType TYPE = RewardTypePatch.RandomOrisonReward;
    private static final Texture ICON = TexLoader.getTexture(makeRewardPath(TYPE.name() + ".png"));
    private static final String TEXT = CardCrawlGame.languagePack.getUIString(makeID(TYPE.name())).TEXT[0];

    public static final boolean ADV = false;
    public int amount;

    public RandomOrisonReward(int amount) {
        super(ICON, TEXT, TYPE);
        this.amount = amount;

        Pair<List<AbstractCard>, List<AbstractCard>> cardsRolled = getCardsWithoutOrisonFirst(
                AbstractDungeon.player.masterDeck.group, amount, AbstractDungeon.cardRandomRng);
        List<AbstractCard> noOrisoCards = cardsRolled.getKey();
        List<AbstractCard> withOrisoCards = cardsRolled.getValue();

        orisons = OrisonLib.getRandomCommonOrison(ADV, noOrisoCards.size(), false);
        if (orisons.size() < noOrisoCards.size())
            orisons.addAll(OrisonLib.getRandomCommonOrison(ADV, noOrisoCards.size() - orisons.size(), true));
        orisons.forEach(o -> logger.debug("Rolled orison: " + o.id + " on no-orison card"));

        for (AbstractCard c : withOrisoCards) {
            List<AbstractCardModifier> oldOrisons = CardModifierManager.modifiers(c)
                    .stream().filter(AbstractOrison.class::isInstance).collect(Collectors.toList());
            oldOrisons.forEach(o -> logger.debug("card " + c.cardID + " has orison: " + o.identifier(c)));
            List<AbstractOrison> newOrisons = OrisonLib.getRandomCommonOrison(ADV, 1, false, oldOrisons::contains);
            if (newOrisons == null || newOrisons.isEmpty()) {
                logger.error("Rolled orison is NULL or EMPTY on with-orison card: " + c.cardID);
                continue;
            }
            newOrisons.forEach(o -> logger.debug("Rolled orison: " + o.id + " on with-orison card: " + c.cardID));
            orisons.addAll(newOrisons);
        }

        cardsToApplyOrison = new ArrayList<>();
        cardsToApplyOrison.addAll(noOrisoCards);
        cardsToApplyOrison.addAll(withOrisoCards);

        initializeCardsToDisplay();
    }
}
