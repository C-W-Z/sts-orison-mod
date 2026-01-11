package orison.core.configs;

import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.vdurmont.semver4j.Semver;

import static orison.core.OrisonMod.info;
import static orison.core.OrisonMod.modID;
import orison.core.abstracts.AbstractOrison.UseType;
import orison.utils.GeneralUtils;
import static orison.utils.GeneralUtils.clamp;

public class OrisonConfig {

    private static final Logger logger = LogManager.getLogger(OrisonConfig.class);

    public static class Version {
        public static SpireConfig config;
        public static String ID_MOD_VERSION = "MOD_VERSION";
        public static String OLD_VERSION;

        public static String ID_HAS_NOTICE = "HAS_NOTICE";
        public static boolean HAS_NOTICE = true;

        public static void initialize() {
            try {
                Properties defaults = new Properties();
                defaults.setProperty(ID_HAS_NOTICE, String.valueOf(HAS_NOTICE));
                config = new SpireConfig(modID, Version.class.getSimpleName(), defaults);
            } catch (Exception e) {
                logger.error("OrisonConfig.Version.initialize() failed");
                e.printStackTrace();
            }

            OLD_VERSION = null;
            if (config.has(ID_MOD_VERSION))
                OLD_VERSION = config.getString(ID_MOD_VERSION);

            if (isVersionUpdate()) {
                logger.info("Detect an Version Update!");
                saveConfig(config, ID_MOD_VERSION, info.ModVersion.toString());
                HAS_NOTICE = true;
                saveConfig(config, ID_HAS_NOTICE, HAS_NOTICE);
            }

            if (config.has(ID_HAS_NOTICE))
                HAS_NOTICE = config.getBool(ID_HAS_NOTICE);
        }

        public static boolean isVersionUpdate() {
            return OLD_VERSION == null || new Semver(OLD_VERSION).isLowerThan(info.ModVersion);
        }

        public static void cancelNotice() {
            HAS_NOTICE = false;
            saveConfig(config, ID_HAS_NOTICE, HAS_NOTICE);
        }
    }

    public static class Preference {
        public static SpireConfig config;

        public static String ID_CONFIG_SCREEN_BG = "CONFIG_SCREEN_BG";
        public static int CONFIG_SCREEN_BG = 0;

        public static void initialize() {
            try {
                Properties defaults = new Properties();
                defaults.setProperty(ID_CONFIG_SCREEN_BG, String.valueOf(CONFIG_SCREEN_BG));
                config = new SpireConfig(modID, Preference.class.getSimpleName(), defaults);
            } catch (Exception e) {
                logger.error("OrisonConfig.Preference.initialize() failed");
                e.printStackTrace();
            }

            loadConfigs();
        }

        public static void loadConfigs() {
            if (config == null)
                return;
            CONFIG_SCREEN_BG = config.getInt(ID_CONFIG_SCREEN_BG);
        }

        public static void save(String ID, int value) {
            saveConfig(config, ID, value);
            loadConfigs();
        }
    }

    public static class Orison {
        public static SpireConfig config;

        public static String ID_CAN_ATTACH_ON_UNPLAYABLE_CARD = "CAN_ATTACH_ON_UNPLAYABLE_CARD";
        public static String ID_CAN_ATTACH_ON_STATUS = "CAN_ATTACH_ON_STATUS";
        public static String ID_CAN_ATTACH_ON_CURSE = "CAN_ATTACH_ON_CURSE";
        public static String ID_CAN_ATTACH_ON_COLORLESS = "CAN_ATTACH_ON_COLORLESS";

        public static boolean CAN_ATTACH_ON_UNPLAYABLE_CARD = false;
        public static boolean CAN_ATTACH_ON_STATUS = false;
        public static boolean CAN_ATTACH_ON_CURSE = false;
        public static boolean CAN_ATTACH_ON_COLORLESS = true;

        public static void initialize() {
            try {
                Properties defaults = new Properties();
                defaults.setProperty(ID_CAN_ATTACH_ON_UNPLAYABLE_CARD, String.valueOf(CAN_ATTACH_ON_UNPLAYABLE_CARD));
                defaults.setProperty(ID_CAN_ATTACH_ON_STATUS, String.valueOf(CAN_ATTACH_ON_STATUS));
                defaults.setProperty(ID_CAN_ATTACH_ON_CURSE, String.valueOf(CAN_ATTACH_ON_CURSE));
                defaults.setProperty(ID_CAN_ATTACH_ON_COLORLESS, String.valueOf(CAN_ATTACH_ON_COLORLESS));
                config = new SpireConfig(modID, Orison.class.getSimpleName(), defaults);
            } catch (Exception e) {
                logger.error("OrisonConfig.Orison.initialize() failed");
                e.printStackTrace();
            }

            loadConfigs();
        }

        public static void loadConfigs() {
            if (config == null)
                return;
            CAN_ATTACH_ON_UNPLAYABLE_CARD = config.getBool(ID_CAN_ATTACH_ON_UNPLAYABLE_CARD);
            CAN_ATTACH_ON_STATUS = config.getBool(ID_CAN_ATTACH_ON_STATUS);
            CAN_ATTACH_ON_CURSE = config.getBool(ID_CAN_ATTACH_ON_CURSE);
            CAN_ATTACH_ON_COLORLESS = config.getBool(ID_CAN_ATTACH_ON_COLORLESS);
        }

        public static void save(String ID, boolean value) {
            saveConfig(config, ID, value);
            loadConfigs();
        }
    }

    public static class OrisonValues {
        public static SpireConfig config;

        public static void initialize() {
            try {
                Properties defaults = new Properties();
                config = new SpireConfig(modID, OrisonValues.class.getSimpleName(), defaults);
            } catch (Exception e) {
                logger.error("OrisonConfig.OrisonValues.initialize() failed");
                e.printStackTrace();
            }
        }

        public static void save(String orisonID, boolean adv, List<Integer> values) {
            saveConfig(config, makeOrisonIDWithAdv(orisonID, adv), GeneralUtils.listToString(values));
        }

        public static List<Integer> load(String orisonID, boolean adv) {
            String id = makeOrisonIDWithAdv(orisonID, adv);
            if (!config.has(id))
                return null;
            return GeneralUtils.stringToIntList(config.getString(id));
        }
    }

    public static class OrisonUseType {
        public static SpireConfig config;

        public static void initialize() {
            try {
                Properties defaults = new Properties();
                config = new SpireConfig(modID, OrisonUseType.class.getSimpleName(), defaults);
            } catch (Exception e) {
                logger.error("OrisonConfig.OrisonUseType.initialize() failed");
                e.printStackTrace();
            }
        }

        public static void save(String orisonID, boolean adv, UseType type) {
            saveConfig(config, makeOrisonIDWithAdv(orisonID, adv), type.name());
        }

        public static UseType load(String orisonID, boolean adv) {
            String id = makeOrisonIDWithAdv(orisonID, adv);
            if (!config.has(id))
                return null;
            String s = config.getString(id);
            if (s == null || s.isEmpty())
                return null;
            try {
                return UseType.valueOf(s);
            } catch (Exception e) {
                logger.error("Parse UseType of Orison: " + orisonID + " Failed: " + s + ", Error: " + e);
                return null;
            }
        }
    }

    public static class OrisonUseLimit {
        public static SpireConfig config;

        public static void initialize() {
            try {
                Properties defaults = new Properties();
                config = new SpireConfig(modID, OrisonUseLimit.class.getSimpleName(), defaults);
            } catch (Exception e) {
                logger.error("OrisonConfig.OrisonUseLimit.initialize() failed");
                e.printStackTrace();
            }
        }

        public static void save(String orisonID, boolean adv, int uses) {
            saveConfig(config, makeOrisonIDWithAdv(orisonID, adv), uses);
        }

        public static Integer load(String orisonID, boolean adv) {
            String id = makeOrisonIDWithAdv(orisonID, adv);
            if (!config.has(id))
                return null;
            return config.getInt(id);
        }
    }

    public static class OrisonRarity {
        public static SpireConfig config;

        public static void initialize() {
            try {
                Properties defaults = new Properties();
                config = new SpireConfig(modID, OrisonRarity.class.getSimpleName(), defaults);
            } catch (Exception e) {
                logger.error("OrisonConfig.OrisonRarity.initialize() failed");
                e.printStackTrace();
            }
        }

        public static void save(String orisonID, float rarity) {
            saveConfig(config, orisonID, rarity);
        }

        public static Float load(String orisonID) {
            if (!config.has(orisonID))
                return null;
            return config.getFloat(orisonID);
        }
    }

    public static class EventEnable {
        public static SpireConfig config;

        public static void initialize() {
            try {
                Properties defaults = new Properties();
                config = new SpireConfig(modID, EventEnable.class.getSimpleName(), defaults);
            } catch (Exception e) {
                logger.error("OrisonConfig.EventEnable.initialize() failed");
                e.printStackTrace();
            }
        }

        public static void save(String eventID, boolean enabled) {
            saveConfig(config, eventID, enabled);
        }

        public static Boolean load(String eventID) {
            if (!config.has(eventID))
                return null;
            return config.getBool(eventID);
        }
    }

    public static class Reward {
        public static SpireConfig config;

        public static final String ID_MONSTER_DROP_ORISON_CHANCE = "MONSTER_DROP_ORISON_CHANCE";
        public static final String ID_MONSTER_DROP_ORISON_ADV_CHANCE = "MONSTER_DROP_ORISON_ADV_CHANCE";
        public static final String ID_MONSTER_DROP_ORISON_LINKED = "MONSTER_DROP_ORISON_LINKED";

        public static final String ID_ELITE_DROP_ORISON_CHANCE = "ELITE_DROP_ORISON_CHANCE";
        public static final String ID_ELITE_DROP_ORISON_ADV_CHANCE = "ELITE_DROP_ORISON_ADV_CHANCE";
        public static final String ID_ELITE_DROP_ORISON_LINKED = "ELITE_DROP_ORISON_LINKED";

        public static final String ID_BOSS_DROP_ORISON_CHANCE = "BOSS_DROP_ORISON_CHANCE";
        public static final String ID_BOSS_DROP_ORISON_ADV_CHANCE = "BOSS_DROP_ORISON_ADV_CHANCE";
        public static final String ID_BOSS_DROP_ORISON_LINKED = "BOSS_DROP_ORISON_LINKED";

        public static final String ID_TREASURE_DROP_ORISON_CHANCE = "TREASURE_DROP_ORISON_CHANCE";
        public static final String ID_TREASURE_DROP_ORISON_ADV_CHANCE = "TREASURE_DROP_ORISON_ADV_CHANCE";

        public static float MONSTER_DROP_ORISON_CHANCE = 0.25F;
        public static float MONSTER_DROP_ORISON_ADV_CHANCE = 0F;
        public static boolean MONSTER_DROP_ORISON_LINKED = true;

        public static float ELITE_DROP_ORISON_CHANCE = 1F;
        public static float ELITE_DROP_ORISON_ADV_CHANCE = 0.5F;
        public static boolean ELITE_DROP_ORISON_LINKED = true;

        public static float BOSS_DROP_ORISON_CHANCE = 1F;
        public static float BOSS_DROP_ORISON_ADV_CHANCE = 1F;
        public static boolean BOSS_DROP_ORISON_LINKED = false;

        public static float TREASURE_DROP_ORISON_CHANCE = 1F;
        public static float TREASURE_DROP_ORISON_ADV_CHANCE = 0.25F;

        public static void initialize() {
            try {
                Properties defaults = new Properties();
                defaults.setProperty(ID_MONSTER_DROP_ORISON_CHANCE, String.valueOf(MONSTER_DROP_ORISON_CHANCE));
                defaults.setProperty(ID_MONSTER_DROP_ORISON_ADV_CHANCE, String.valueOf(MONSTER_DROP_ORISON_ADV_CHANCE));
                defaults.setProperty(ID_MONSTER_DROP_ORISON_LINKED, String.valueOf(MONSTER_DROP_ORISON_LINKED));

                defaults.setProperty(ID_ELITE_DROP_ORISON_CHANCE, String.valueOf(ELITE_DROP_ORISON_CHANCE));
                defaults.setProperty(ID_ELITE_DROP_ORISON_ADV_CHANCE, String.valueOf(ELITE_DROP_ORISON_ADV_CHANCE));
                defaults.setProperty(ID_ELITE_DROP_ORISON_LINKED, String.valueOf(ELITE_DROP_ORISON_LINKED));

                defaults.setProperty(ID_BOSS_DROP_ORISON_CHANCE, String.valueOf(BOSS_DROP_ORISON_CHANCE));
                defaults.setProperty(ID_BOSS_DROP_ORISON_ADV_CHANCE, String.valueOf(BOSS_DROP_ORISON_ADV_CHANCE));
                defaults.setProperty(ID_BOSS_DROP_ORISON_LINKED, String.valueOf(BOSS_DROP_ORISON_LINKED));

                defaults.setProperty(ID_TREASURE_DROP_ORISON_CHANCE, String.valueOf(TREASURE_DROP_ORISON_CHANCE));
                defaults.setProperty(ID_TREASURE_DROP_ORISON_ADV_CHANCE,
                        String.valueOf(TREASURE_DROP_ORISON_ADV_CHANCE));
                config = new SpireConfig(modID, Reward.class.getSimpleName(), defaults);
            } catch (Exception e) {
                logger.error("OrisonConfig.Reward.initialize() failed");
                e.printStackTrace();
            }

            loadConfigs();
        }

        public static void loadConfigs() {
            if (config == null)
                return;
            MONSTER_DROP_ORISON_CHANCE = config.getFloat(ID_MONSTER_DROP_ORISON_CHANCE);
            MONSTER_DROP_ORISON_ADV_CHANCE = config.getFloat(ID_MONSTER_DROP_ORISON_ADV_CHANCE);
            MONSTER_DROP_ORISON_LINKED = config.getBool(ID_MONSTER_DROP_ORISON_LINKED);

            ELITE_DROP_ORISON_CHANCE = config.getFloat(ID_ELITE_DROP_ORISON_CHANCE);
            ELITE_DROP_ORISON_ADV_CHANCE = config.getFloat(ID_ELITE_DROP_ORISON_ADV_CHANCE);
            ELITE_DROP_ORISON_LINKED = config.getBool(ID_ELITE_DROP_ORISON_LINKED);

            BOSS_DROP_ORISON_CHANCE = config.getFloat(ID_BOSS_DROP_ORISON_CHANCE);
            BOSS_DROP_ORISON_ADV_CHANCE = config.getFloat(ID_BOSS_DROP_ORISON_ADV_CHANCE);
            BOSS_DROP_ORISON_LINKED = config.getBool(ID_BOSS_DROP_ORISON_LINKED);

            TREASURE_DROP_ORISON_CHANCE = config.getFloat(ID_TREASURE_DROP_ORISON_CHANCE);
            TREASURE_DROP_ORISON_ADV_CHANCE = config.getFloat(ID_TREASURE_DROP_ORISON_ADV_CHANCE);

            MONSTER_DROP_ORISON_CHANCE = clamp(MONSTER_DROP_ORISON_CHANCE, 0, 1);
            MONSTER_DROP_ORISON_ADV_CHANCE = clamp(MONSTER_DROP_ORISON_ADV_CHANCE, 0, 1);

            ELITE_DROP_ORISON_CHANCE = clamp(ELITE_DROP_ORISON_CHANCE, 0, 1);
            ELITE_DROP_ORISON_ADV_CHANCE = clamp(ELITE_DROP_ORISON_ADV_CHANCE, 0, 1);

            BOSS_DROP_ORISON_CHANCE = clamp(BOSS_DROP_ORISON_CHANCE, 0, 1);
            BOSS_DROP_ORISON_ADV_CHANCE = clamp(BOSS_DROP_ORISON_ADV_CHANCE, 0, 1);

            TREASURE_DROP_ORISON_CHANCE = clamp(TREASURE_DROP_ORISON_CHANCE, 0, 1);
            TREASURE_DROP_ORISON_ADV_CHANCE = clamp(TREASURE_DROP_ORISON_ADV_CHANCE, 0, 1);
        }

        public static void save(String ID, boolean value) {
            if (config == null)
                return;
            saveConfig(config, ID, value);
            loadConfigs();
        }

        public static void save(String ID, float value) {
            if (config == null)
                return;
            saveConfig(config, ID, value);
            loadConfigs();
        }
    }

    public static void initialize() {
        Version.initialize();
        Preference.initialize();
        Orison.initialize();
        Reward.initialize();
        OrisonRarity.initialize();
        OrisonValues.initialize();
        OrisonUseType.initialize();
        OrisonUseLimit.initialize();
        EventEnable.initialize();
    }

    public static String makeOrisonIDWithAdv(String orisonID, boolean adv) {
        return adv ? (orisonID + "_adv") : orisonID;
    }

    public static void saveConfig(SpireConfig config, String ID, boolean value) {
        try {
            config.setBool(ID, value);
            config.save();
        } catch (Exception e) {
            logger.error("Save config " + ID + "=" + value + " Failed!");
            e.printStackTrace();
        }
    }

    public static void saveConfig(SpireConfig config, String ID, int value) {
        try {
            config.setInt(ID, value);
            config.save();
        } catch (Exception e) {
            logger.error("Save config " + ID + "=" + value + " Failed!");
            e.printStackTrace();
        }
    }

    public static void saveConfig(SpireConfig config, String ID, float value) {
        try {
            config.setFloat(ID, value);
            config.save();
        } catch (Exception e) {
            logger.error("Save config " + ID + "=" + value + " Failed!");
            e.printStackTrace();
        }
    }

    public static void saveConfig(SpireConfig config, String ID, String value) {
        try {
            config.setString(ID, value);
            config.save();
        } catch (Exception e) {
            logger.error("Save config " + ID + "=" + value + " Failed!");
            e.printStackTrace();
        }
    }
}
