package orison.core.configs;

import static orison.core.OrisonMod.modID;
import static orison.utils.GeneralUtils.clamp;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;

public class OrisonConfig {

    private static final Logger logger = LogManager.getLogger(OrisonConfig.class);

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
                defaults.setProperty(ID_TREASURE_DROP_ORISON_ADV_CHANCE, String.valueOf(TREASURE_DROP_ORISON_ADV_CHANCE));
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
        Preference.initialize();
        Reward.initialize();
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
}
