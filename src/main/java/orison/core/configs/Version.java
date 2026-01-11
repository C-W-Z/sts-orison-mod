package orison.core.configs;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.vdurmont.semver4j.Semver;

import static orison.core.OrisonMod.info;
import static orison.core.OrisonMod.modID;

public class Version {

    private static final Logger logger = LogManager.getLogger(Version.class);

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
            OrisonConfig.saveConfig(config, ID_MOD_VERSION, info.ModVersion.toString());
            HAS_NOTICE = true;
            OrisonConfig.saveConfig(config, ID_HAS_NOTICE, HAS_NOTICE);

            if (new Semver(OLD_VERSION).isLowerThan(new Semver("4.0.0"))) {
                JsonConfig.convertSpireConfigToJson();
                OrisonConfig.clear();
                OrisonConfig.save();
            }
        }

        if (config.has(ID_HAS_NOTICE))
            HAS_NOTICE = config.getBool(ID_HAS_NOTICE);
    }

    public static boolean isVersionUpdate() {
        return OLD_VERSION == null || new Semver(OLD_VERSION).isLowerThan(info.ModVersion);
    }

    public static void cancelNotice() {
        HAS_NOTICE = false;
        OrisonConfig.saveConfig(config, ID_HAS_NOTICE, HAS_NOTICE);
    }
}
