package orison.core.configs;

import static orison.core.OrisonMod.modID;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import orison.core.abstracts.AbstractOrison;
import orison.core.configs.JsonConfigData.OrisonData;
import orison.core.libs.OrisonLib;

// 用Json取代SpireConfig，然後將整個架構改成Momento設計模式
public class JsonConfig {

    private static final Logger logger = LogManager.getLogger(JsonConfig.class);

    // currunt config
    private static JsonConfigData config = new JsonConfigData();
    private static String curConfigFile = "config";

    // public static OrisonData defaultOrisonData = new OrisonData(
    // ErrorOrison.ID, new ArrayList<>(), UseType.INFINITE, -1, 0F);

    // config save slots
    public static List<JsonConfigData> defaultConfigData = new ArrayList<>();
    public static List<JsonConfigData> customConfigData = new ArrayList<>();

    public static void loadDefaultConfigs() {
        defaultConfigData.add(getDefaultConfig(0));
        defaultConfigData.add(getDefaultConfig(1));
        customConfigData.add(getDefaultConfig(0));
        customConfigData.add(getDefaultConfig(1));
    }

    public static JsonConfigData getDefaultConfig(int index) {
        String jsonString = Gdx.files.internal(String.format("%sResources/configs/default%d.json", modID, index))
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Gson gson = new GsonBuilder()
                .setPrettyPrinting() // 可選，方便人看
                .create();
        return gson.fromJson(jsonString, JsonConfigData.class);
    }

    public static JsonConfigData getConfig() {
        return config;
    }

    public static void setConfig(JsonConfigData data) {
        config = data;
    }

    public static void convertSpireConfigToJson() {
        JsonConfigData newConfig = new JsonConfigData();

        newConfig.CONFIG_SCREEN_BG = OrisonConfig.Preference.config
                .getInt(OrisonConfig.Preference.ID_CONFIG_SCREEN_BG);

        newConfig.CAN_ATTACH_ON_UNPLAYABLE_CARD = OrisonConfig.Orison.config
                .getBool(OrisonConfig.Orison.ID_CAN_ATTACH_ON_UNPLAYABLE_CARD);
        newConfig.CAN_ATTACH_ON_STATUS = OrisonConfig.Orison.config
                .getBool(OrisonConfig.Orison.ID_CAN_ATTACH_ON_STATUS);
        newConfig.CAN_ATTACH_ON_CURSE = OrisonConfig.Orison.config
                .getBool(OrisonConfig.Orison.ID_CAN_ATTACH_ON_CURSE);
        newConfig.CAN_ATTACH_ON_COLORLESS = OrisonConfig.Orison.config
                .getBool(OrisonConfig.Orison.ID_CAN_ATTACH_ON_COLORLESS);

        newConfig.MONSTER_DROP_ORISON_CHANCE = OrisonConfig.Reward.config
                .getFloat(OrisonConfig.Reward.ID_MONSTER_DROP_ORISON_CHANCE);
        newConfig.MONSTER_DROP_ORISON_ADV_CHANCE = OrisonConfig.Reward.config
                .getFloat(OrisonConfig.Reward.ID_MONSTER_DROP_ORISON_ADV_CHANCE);
        newConfig.MONSTER_DROP_ORISON_LINKED = OrisonConfig.Reward.config
                .getBool(OrisonConfig.Reward.ID_MONSTER_DROP_ORISON_LINKED);

        newConfig.ELITE_DROP_ORISON_CHANCE = OrisonConfig.Reward.config
                .getFloat(OrisonConfig.Reward.ID_ELITE_DROP_ORISON_CHANCE);
        newConfig.ELITE_DROP_ORISON_ADV_CHANCE = OrisonConfig.Reward.config
                .getFloat(OrisonConfig.Reward.ID_ELITE_DROP_ORISON_ADV_CHANCE);
        newConfig.ELITE_DROP_ORISON_LINKED = OrisonConfig.Reward.config
                .getBool(OrisonConfig.Reward.ID_ELITE_DROP_ORISON_LINKED);

        newConfig.BOSS_DROP_ORISON_CHANCE = OrisonConfig.Reward.config
                .getFloat(OrisonConfig.Reward.ID_BOSS_DROP_ORISON_CHANCE);
        newConfig.BOSS_DROP_ORISON_ADV_CHANCE = OrisonConfig.Reward.config
                .getFloat(OrisonConfig.Reward.ID_BOSS_DROP_ORISON_ADV_CHANCE);
        newConfig.BOSS_DROP_ORISON_LINKED = OrisonConfig.Reward.config
                .getBool(OrisonConfig.Reward.ID_BOSS_DROP_ORISON_LINKED);

        newConfig.TREASURE_DROP_ORISON_CHANCE = OrisonConfig.Reward.config
                .getFloat(OrisonConfig.Reward.ID_TREASURE_DROP_ORISON_CHANCE);
        newConfig.TREASURE_DROP_ORISON_ADV_CHANCE = OrisonConfig.Reward.config
                .getFloat(OrisonConfig.Reward.ID_TREASURE_DROP_ORISON_ADV_CHANCE);

        for (AbstractOrison o : OrisonLib.allOrisons) {
            newConfig.orisonDataMap.put(
                    o.id, new OrisonData(o.id,
                            OrisonConfig.OrisonValues.load(o.id, false),
                            OrisonConfig.OrisonUseType.load(o.id, false),
                            OrisonConfig.OrisonUseLimit.load(o.id, false),
                            OrisonConfig.OrisonRarity.load(o.id)));
            newConfig.advOrisonDataMap.put(
                    o.id, new OrisonData(o.id,
                            OrisonConfig.OrisonValues.load(o.id, true),
                            OrisonConfig.OrisonUseType.load(o.id, true),
                            OrisonConfig.OrisonUseLimit.load(o.id, true),
                            null));
        }

        // save(newConfig, "test");
        // save(merge(defaultConfigData.get(0), newConfig), "merge");
        config = merge(defaultConfigData.get(0), newConfig);
        customConfigData.set(0, config.copy());
        save();
        saveCustomPreset(0);
    }

    public static void save() {
        save(config, curConfigFile);
    }

    public static void saveCustomPreset(int index) {
        save(customConfigData.get(index), "customConfig" + index);
    }

    public static void save(JsonConfigData data, String fileName) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting() // 可選，方便人看
                .create();

        try (FileWriter writer = new FileWriter(SpireConfig.makeFilePath(modID, fileName, "json"))) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            logger.error("JsonConfig.save() Failed!");
            e.printStackTrace();
        }
    }

    public static void load() {
        config = load(curConfigFile);
    }

    public static JsonConfigData load(String fileName) {
        Gson gson = new Gson();

        JsonConfigData data = null;
        try (Reader reader = new FileReader(SpireConfig.makeFilePath(modID, fileName, "json"))) {
            data = gson.fromJson(reader, JsonConfigData.class);
        } catch (FileNotFoundException e) {
            data = getDefaultConfig(1);
            save();
            logger.error("JsonConfig.load() File Not Found, create default config...");
            // e.printStackTrace();
        } catch (IOException e) {
            logger.error("JsonConfig.load() Failed!");
            e.printStackTrace();
        }

        return data;
    }

    // baseConfig == defaultConfig, newConfig == userConfig
    public static JsonConfigData merge(JsonConfigData baseConfig, JsonConfigData newConfig) {
        JsonConfigData result = newConfig.copy();
        for (AbstractOrison o : OrisonLib.allOrisons) {
            OrisonData baseData = baseConfig.orisonDataMap.get(o.id);
            if (!result.orisonDataMap.containsKey(o.id))
                result.orisonDataMap.put(o.id, baseData);
            OrisonData data = result.orisonDataMap.get(o.id);
            if (data.values == null || data.values.size() != baseData.values.size())
                data.values = new ArrayList<>(baseData.values);
            if (data.useType == null)
                data.useType = baseData.useType;
            if (data.useLimit == null || data.useLimit < 0)
                data.useLimit = baseData.useLimit;
            if (data.rarity == null || data.rarity < 0)
                data.rarity = baseData.rarity;

            baseData = baseConfig.advOrisonDataMap.get(o.id);
            if (!result.advOrisonDataMap.containsKey(o.id))
                result.advOrisonDataMap.put(o.id, baseData);
            data = result.advOrisonDataMap.get(o.id);
            if (data.values == null || data.values.size() != baseData.values.size())
                data.values = new ArrayList<>(baseData.values);
            if (data.useType == null)
                data.useType = baseData.useType;
            if (data.useLimit == null || data.useLimit < 0)
                data.useLimit = baseData.useLimit;
            if (data.rarity == null || data.rarity < 0)
                data.rarity = baseData.rarity;
        }
        for (String eventID : baseConfig.eventEnabledMap.keySet()) {
            if (!newConfig.eventEnabledMap.containsKey(eventID))
                newConfig.eventEnabledMap.put(eventID, baseConfig.eventEnabledMap.get(eventID));
        }
        return result;
    }

    public static void saveOrisonData(AbstractOrison orison) {
        (orison.adv ? config.orisonDataMap : config.advOrisonDataMap)
                .put(orison.id, new OrisonData(orison.id, orison.getValueList(),
                        orison.getUseType(), orison.getUseLimit(), orison.getRarity()));
    }
}
