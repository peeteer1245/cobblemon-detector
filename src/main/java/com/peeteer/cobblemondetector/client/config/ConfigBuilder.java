package com.peeteer.cobblemondetector.client.config;

import com.google.gson.GsonBuilder;
import com.peeteer.cobblemondetector.client.CobblemonDetectorClient;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConfigBuilder {
    public static CobblemonDetectorConfig make(){
        Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

        CobblemonDetectorConfig config = new CobblemonDetectorConfig();
        File configFile = new File("config/CobblemonDetector.json");
        configFile.getParentFile().mkdirs();

        if (configFile.exists()) {
            try {
                FileReader fileReader = new FileReader(configFile);
                config = gson.fromJson(fileReader, CobblemonDetectorConfig.class);
                fileReader.close();
            } catch (Exception e) {
                CobblemonDetectorClient.LOGGER.info("unable to read config file", e);
            }
        } else {
            try {
                FileWriter fileWriter = new FileWriter(configFile);
                gson.toJson(config, fileWriter);
                fileWriter.close();
            } catch (Exception e) {
                CobblemonDetectorClient.LOGGER.error("unable to write config file", e);
            }
        }
        return config;
    }
}