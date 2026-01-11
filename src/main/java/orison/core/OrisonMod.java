package orison.core;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import orison.cardvars.AbstractEasyDynamicVariable;
import orison.core.abstracts.AbstractEasyCard;
import orison.core.abstracts.AbstractEasyPotion;
import orison.core.abstracts.AbstractOrison;
import orison.core.abstracts.AbstractOrisonRelic;
import orison.core.configs.JsonConfig;
import orison.core.configs.JsonConfigData;
import orison.core.configs.JsonConfigData.OrisonData;
import orison.core.configs.OrisonConfig;
import orison.core.configs.OrisonConfig.Orison;
import orison.core.interfaces.OrisonExtension;
import orison.core.libs.EventLib;
import orison.core.libs.OrisonLib;
import orison.core.libs.RewardLib;
import orison.core.savables.OrisonRng;
import orison.core.savables.OrisonSave;
import orison.extensions.morimensmod.MorimensModExtension;
import orison.extensions.vanilla.VanillaExtension;
import orison.utils.ProAudio;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import static orison.utils.GeneralUtils.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

@SpireInitializer
public class OrisonMod implements
        PostDungeonInitializeSubscriber,
        PostInitializeSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        AddAudioSubscriber {

    private static final Logger logger = LogManager.getLogger(OrisonMod.class);

    public static final ModInfo info;
    public static final String modID;
    public static final List<OrisonExtension> orisonExtensions = new ArrayList<>();

    static {
        /**
         * This determines the mod's ID based on information stored by ModTheSpire.
         */
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo) -> {
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(),
                    Collections.emptySet());
            return initializers.contains(OrisonMod.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;

            logger.info("ModID: " + modID);
            logger.info("Name: " + info.Name);
            logger.info("Version: " + info.ModVersion);
            logger.info("Authors: " + arrToString(info.Authors));
            logger.info("Description: " + info.Description);
        } else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public static Settings.GameLanguage[] SupportedLanguages = {
            Settings.GameLanguage.ENG,
            Settings.GameLanguage.ZHS,
            Settings.GameLanguage.ZHT,
    };

    private String getLangString() {
        for (Settings.GameLanguage lang : SupportedLanguages)
            if (lang.equals(Settings.language))
                return Settings.language.name().toLowerCase();
        return Settings.GameLanguage.ENG.name().toLowerCase();
    }

    public static String makePath(String resourcePath) {
        return modID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return modID + "Resources/images/" + resourcePath;
    }

    public static String makeIconPath(String resourcePath) {
        return modID + "Resources/images/icons/" + resourcePath;
    }

    public static String makeOrisonPath(String resourcePath) {
        return modID + "Resources/images/orisons/" + resourcePath;
    }

    public static String makeRewardPath(String resourcePath) {
        return modID + "Resources/images/rewards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return modID + "Resources/images/relics/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return modID + "Resources/images/powers/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return modID + "Resources/images/cards/" + resourcePath;
    }

    public static String makeUIPath(String resourcePath) {
        return modID + "Resources/images/ui/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return modID + "Resources/images/events/" + resourcePath;
    }

    public static void initialize() {
        new OrisonMod();
    }

    public OrisonMod() {
        BaseMod.subscribe(this);
    }

    @Override
    public void receiveEditCharacters() {
        new AutoAdd(modID)
                .packageFilter(AbstractEasyPotion.class)
                .any(AbstractEasyPotion.class, (info, potion) -> {
                    if (potion.pool == null)
                        BaseMod.addPotion(potion.getClass(), potion.liquidColor, potion.hybridColor, potion.spotsColor,
                                potion.ID);
                    else
                        BaseMod.addPotion(potion.getClass(), potion.liquidColor, potion.hybridColor, potion.spotsColor,
                                potion.ID, potion.pool);
                });
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
                .packageFilter("orison.core.relics")
                .any(AbstractOrisonRelic.class, (info, relic) -> {
                    logger.info("add relic " + relic.relicId);
                    BaseMod.addRelic(relic, RelicType.SHARED);
                    if (!info.seen)
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                });
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd(modID)
                .packageFilter(AbstractEasyDynamicVariable.class)
                .any(DynamicVariable.class, (info, var) -> BaseMod.addDynamicVariable(var));
        new AutoAdd(modID)
                .packageFilter(AbstractEasyCard.class)
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(CardStrings.class,
                modID + "Resources/localization/" + getLangString() + "/Cardstrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                modID + "Resources/localization/" + getLangString() + "/Relicstrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                modID + "Resources/localization/" + getLangString() + "/Powerstrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class,
                modID + "Resources/localization/" + getLangString() + "/UIstrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                modID + "Resources/localization/" + getLangString() + "/Potionstrings.json");
        BaseMod.loadCustomStringsFile(EventStrings.class,
                modID + "Resources/localization/" + getLangString() + "/Eventstrings.json");
    }

    @Override
    public void receiveAddAudio() {
        for (ProAudio a : ProAudio.values())
            BaseMod.addAudio(makeID(a.name()), makePath("audio/" + a.name().toLowerCase() + ".ogg"));
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(modID + "Resources/localization/" + getLangString() + "/Keywordstrings.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json,
                com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receivePostInitialize() {
        OrisonConfig.initialize();
        initializeOrisonExtensions();
        OrisonLib.initialize();
        RewardLib.initialize();
        BaseMod.addSaveField(OrisonSave.ID, new OrisonSave());
        BaseMod.addSaveField(OrisonRng.ID, new OrisonRng());
        EventLib.initialize();
        // Settings.isInfo = true;
        JsonConfig.convertSpireConfigToJson();
        // JsonConfig.load();
        // JsonConfig.save();
    }

    /** 如果有其他模組想要新增OrisonExtension，就直接SpirePostfixPatch這個函式即可 */
    public static void initializeOrisonExtensions() {
        new VanillaExtension();
        new MorimensModExtension();
    }

    public static void register(OrisonExtension extension) {
        orisonExtensions.add(extension);
        logger.info(extension.getClass().getName() + " Registered!");
    }

    public static void unregister(OrisonExtension extension) {
        orisonExtensions.remove(extension);
        logger.info(extension.getClass().getName() + " Unregistered!");
    }

    @Override
    public void receivePostDungeonInitialize() {
        OrisonRng.onPostDungeonInitialize();
    }
}
