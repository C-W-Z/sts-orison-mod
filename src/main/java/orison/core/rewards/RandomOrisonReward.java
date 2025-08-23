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
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.TreasureRoom;

import basemod.Pair;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import orison.core.abstracts.AbstractOrison;
import orison.core.abstracts.AbstractOrisonReward;
import orison.core.configs.OrisonConfig;
import orison.core.libs.OrisonLib;
import orison.core.patches.RewardTypePatch;
import orison.core.savables.OrisonRng;
import orison.utils.TexLoader;

public class RandomOrisonReward extends AbstractOrisonReward {

    private static final Logger logger = LogManager.getLogger(RandomOrisonReward.class);

    public static final RewardType TYPE = RewardTypePatch.RandomOrisonReward;
    private static final Texture ICON = TexLoader.getTexture(makeRewardPath(TYPE.name() + ".png"));
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(makeID(TYPE.name())).TEXT;

    public boolean adv;
    public int amount;

    public RandomOrisonReward(int amount) {
        this(ICON, TEXT[0], TYPE, amount, false);
    }

    public RandomOrisonReward(Texture icon, String text, RewardType type, int amount, boolean adv) {
        super(icon, text, type);
        this.amount = amount;
        this.adv = adv;

        Pair<List<AbstractCard>, List<AbstractCard>> cardsRolled = getCardsWithoutOrisonFirst(
                AbstractDungeon.player.masterDeck.group, amount, OrisonRng.get());
        List<AbstractCard> noOrisoCards = cardsRolled.getKey();
        List<AbstractCard> withOrisoCards = cardsRolled.getValue();

        orisons = OrisonLib.getRandomCommonOrison(adv, noOrisoCards.size(), false);
        if (orisons.size() < noOrisoCards.size())
            orisons.addAll(OrisonLib.getRandomCommonOrison(adv, noOrisoCards.size() - orisons.size(), true));
        orisons.forEach(o -> logger.debug("Rolled orison: " + o.id + " on no-orison card"));

        for (AbstractCard c : withOrisoCards) {
            List<AbstractCardModifier> oldOrisons = CardModifierManager.modifiers(c)
                    .stream().filter(AbstractOrison.class::isInstance).collect(Collectors.toList());
            oldOrisons.forEach(o -> logger.debug("card " + c.cardID + " has orison: " + o.identifier(c)));
            List<AbstractOrison> newOrisons = OrisonLib.getRandomCommonOrison(adv, 1, false, o -> {
                for (AbstractCardModifier old : oldOrisons)
                    if (old.identifier(c).equals(o.id) && ((AbstractOrison) old).adv == o.adv)
                        return true;
                return false;
            });
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

        AbstractRoom room = AbstractDungeon.getCurrRoom();
        for (AbstractOrison o : orisons) {
            if (o.adv)
                continue;
            float advChance = 0F;
            if (room instanceof MonsterRoomBoss)
                advChance += OrisonConfig.Reward.BOSS_DROP_ORISON_ADV_CHANCE;
            else if (room instanceof MonsterRoomElite)
                advChance += OrisonConfig.Reward.ELITE_DROP_ORISON_ADV_CHANCE;
            else if (room instanceof MonsterRoom)
                advChance += OrisonConfig.Reward.MONSTER_DROP_ORISON_ADV_CHANCE;
            else if (room instanceof TreasureRoom)
                advChance += OrisonConfig.Reward.TREASURE_DROP_ORISON_ADV_CHANCE;
            o.adv = OrisonRng.get().randomBoolean(advChance);
        }

        initializeCardsToDisplay();
    }
}
