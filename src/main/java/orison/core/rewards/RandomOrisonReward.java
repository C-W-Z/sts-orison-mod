package orison.core.rewards;

import static orison.core.OrisonMod.makeRewardPath;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.TreasureRoom;

import basemod.Pair;
import orison.core.abstracts.AbstractOrison;
import orison.core.abstracts.AbstractOrisonReward;
import orison.core.configs.OrisonConfig;
import orison.core.libs.OrisonLib;
import orison.core.patches.RewardTypePatch;
import orison.core.savables.OrisonRng;
import orison.utils.OrisonHelper;
import orison.utils.TexLoader;

public class RandomOrisonReward extends AbstractOrisonReward {

    private static final Logger logger = LogManager.getLogger(RandomOrisonReward.class);

    public static final RewardType TYPE = RewardTypePatch.RandomOrisonReward;
    private static final Texture ICON = TexLoader.getTexture(makeRewardPath(TYPE.name() + ".png"));

    public boolean adv;
    public int amount;

    public RandomOrisonReward(int amount) {
        this(ICON, TYPE, amount, false);
    }

    public RandomOrisonReward(Texture icon, RewardType type, int amount, boolean adv) {
        super(icon, type);
        this.amount = amount;
        this.adv = adv;

        Pair<List<AbstractCard>, List<AbstractCard>> cardsRolled = getCardsWithoutOrisonFirst(
                AbstractDungeon.player.masterDeck.group, amount, OrisonRng.get());
        List<AbstractCard> noOrisoCards = cardsRolled.getKey();
        List<AbstractCard> withOrisoCards = cardsRolled.getValue();

        orisons = OrisonLib.getRandomCommonOrison(adv, noOrisoCards.size(), false);
        if (orisons.size() < noOrisoCards.size())
            orisons.addAll(OrisonLib.getRandomCommonOrison(adv, noOrisoCards.size() - orisons.size(), true));
        orisons.forEach(o -> logger.info("Rolled orison: " + o.id + " on no-orison card"));

        for (AbstractCard c : withOrisoCards) {
            List<AbstractOrison> oldOrisons = OrisonHelper.getOrisons(c);
            oldOrisons.forEach(o -> logger.info("card " + c.cardID + " has orison: " + o.identifier(c)));
            List<AbstractOrison> newOrisons = OrisonLib.getRandomCommonOrison(adv, 1, false, o -> {
                for (AbstractOrison old : oldOrisons)
                    if (old.id.equals(o.id) && old.adv == o.adv)
                        return true;
                return false;
            });
            if (newOrisons == null || newOrisons.isEmpty()) {
                logger.error("Rolled orison is NULL or EMPTY on with-orison card: " + c.cardID);
                continue;
            }
            newOrisons.forEach(o -> logger.info("Rolled orison: " + o.id + " on with-orison card: " + c.cardID));
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
    }
}
