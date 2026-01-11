package orison.core.configs;

import static orison.core.OrisonMod.modID;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import orison.core.abstracts.AbstractOrison;
import orison.core.abstracts.AbstractOrison.UseType;
import orison.core.configs.JsonConfigData.OrisonData;
import orison.core.libs.EventLib;
import orison.core.libs.OrisonLib;
import orison.misc.ErrorOrison;

// 用Json取代SpireConfig，然後將整個架構改成Momento設計模式
public class JsonConfig {

    private static JsonConfigData config = new JsonConfigData();

    // private File file;
    private static String filePath = SpireConfig.makeFilePath(modID, "config", "json");

    public static OrisonData defaultOrisonData = new OrisonData(
            ErrorOrison.ID, new ArrayList<>(), UseType.INFINITE, -1, 0);

    public static void loadDefaultConfig(int index) {
        String jsonString = Gdx.files.internal(String.format("%sResources/configs/default%d.json", modID, index))
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Gson gson = new GsonBuilder()
                .setPrettyPrinting() // 可選，方便人看
                .create();
        config = gson.fromJson(jsonString, JsonConfigData.class);
    }

    public static JsonConfigData getConfig() {
        return config;
    }

    public static void setConfig(JsonConfigData data) {
        config = data;
    }

    public static void convertSpireConfigToJson() {
        JsonConfig.getConfig().orisonDataMap.clear();
        for (AbstractOrison o : OrisonLib.allOrisons) {
            JsonConfig.getConfig().orisonDataMap.put(
                o.id, new OrisonData(o.id, o.getValueList(), o.getUseType(), o.getUseLimit(), o.getRarity()));
            AbstractOrison ao = o.newInstance(true);
            JsonConfig.getConfig().advOrisonDataMap.put(
                ao.id, new OrisonData(ao.id, ao.getValueList(), ao.getUseType(), ao.getUseLimit(), ao.getRarity()));
        }
        JsonConfig.getConfig().eventEnabledMap.clear();
        JsonConfig.getConfig().eventEnabledMap.putAll(EventLib.enabled);
        JsonConfig.save();
    }

    public static void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting() // 可選，方便人看
                .create();

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void load() {
        Gson gson = new Gson();

        try (Reader reader = new FileReader(filePath)) {
            config = gson.fromJson(reader, JsonConfigData.class);
        } catch (FileNotFoundException e) {
            loadDefaultConfig(0);
            // e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void saveOrisonData(AbstractOrison orison) {
        config.orisonDataMap.put(orison.id, new OrisonData(orison.id, orison.getValueList(), null, 0, 0));
    }
}
