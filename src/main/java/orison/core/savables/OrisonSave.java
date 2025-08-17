package orison.core.savables;

import static orison.core.OrisonMod.makeID;

import java.util.List;
import java.util.stream.Collectors;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import basemod.abstracts.CustomSavable;
import basemod.helpers.CardModifierManager;
import orison.core.abstracts.AbstractOrison;
import orison.core.libs.OrisonLib;

/** 保留擴充性，留有一張牌多個刻印的可能性，以及刻印多次升級的可能性 */
public class OrisonSave implements CustomSavable<List<List<OrisonSave.SaveInfo>>> {

    public static class SaveInfo {
        public String id;
        public int level;

        public SaveInfo(String id, int level) {
            this.id = id;
            this.level = level;
        }
    }

    public static final String ID = makeID(OrisonSave.class.getSimpleName());

    private static List<List<SaveInfo>> orisonsToSave;

    @Override
    public void onLoad(List<List<SaveInfo>> data) {
        if (data == null)
            return;
        for (int i = 0; i < data.size(); i++) {
            AbstractCard card = AbstractDungeon.player.masterDeck.group.get(i);
            data.get(i).forEach(info -> {
                AbstractOrison orison = OrisonLib.getOrison(info.id, info.level > 0);
                if (orison == null)
                    orison = OrisonLib.getErrorOrison();
                CardModifierManager.addModifier(card, orison);
            });
        }
    }

    @Override
    public List<List<SaveInfo>> onSave() {
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
                            .map(m -> new SaveInfo(m.identifier(c), ((AbstractOrison) m).adv ? 1 : 0))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
        }
    }
}
