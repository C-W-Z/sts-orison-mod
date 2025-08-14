package orison.savables;

import static orison.OrisonMod.makeID;

import java.util.List;
import java.util.stream.Collectors;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import basemod.Pair;
import basemod.abstracts.CustomSavable;
import basemod.helpers.CardModifierManager;
import orison.cardmodifiers.AbstractOrison;
import orison.util.OrisonLib;

public class OrisonSave implements CustomSavable<List<List<Pair<String, Boolean>>>> {

    public static final String ID = makeID(OrisonSave.class.getSimpleName());

    private static List<List<Pair<String, Boolean>>> orisonsToSave;

    @Override
    public void onLoad(List<List<Pair<String, Boolean>>> data) {
        if (data == null)
            return;
        for (int i = 0; i < data.size(); i++) {
            AbstractCard card = AbstractDungeon.player.masterDeck.group.get(i);
            data.get(i).forEach(pair -> {
                AbstractOrison orison = OrisonLib.getOrison(pair.getKey(), pair.getValue());
                if (orison != null)
                    CardModifierManager.addModifier(card, orison);
            });
        }
    }

    @Override
    public List<List<Pair<String, Boolean>>> onSave() {
        // reload since basemod will remove all @SaveIgnore cardmods from masterdeck
        onLoad(orisonsToSave);
        return orisonsToSave;
    }

    @SpirePatch2(clz = SaveFile.class, method = SpirePatch.CONSTRUCTOR, paramtypez = { SaveFile.SaveType.class })
    public static class OnSavePatch {
        @SpirePrefixPatch
        public static void Prefix() {
            orisonsToSave = AbstractDungeon.player.masterDeck.group.stream()
                    .map(c -> CardModifierManager.modifiers(c).stream()
                            .filter(AbstractOrison.class::isInstance)
                            .map(m -> new Pair<>(m.identifier(c), ((AbstractOrison) m).adv))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
        }
    }
}
