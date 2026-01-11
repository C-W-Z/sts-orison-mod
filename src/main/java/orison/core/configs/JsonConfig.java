package orison.core.configs;

import static orison.core.OrisonMod.modID;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// 用Json取代SpireConfig，然後將整個架構改成Momento設計模式
public class JsonConfig {

    private static JsonConfigData config = new JsonConfigData();

    // private File file;
    private static String filePath = SpireConfig.makeFilePath(modID, "config_test", "json");

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
