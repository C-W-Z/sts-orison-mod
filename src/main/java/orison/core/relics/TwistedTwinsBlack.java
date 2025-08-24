package orison.core.relics;

import static orison.core.OrisonMod.makeID;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import orison.core.abstracts.AbstractOrisonRelic;
import orison.core.patches.OrisonRewardPatch;

/** @see OrisonRewardPatch */
public class TwistedTwinsBlack extends AbstractOrisonRelic {

    public static final String ID = makeID(TwistedTwinsBlack.class.getSimpleName());

    public TwistedTwinsBlack() {
        super(ID, RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }
}
