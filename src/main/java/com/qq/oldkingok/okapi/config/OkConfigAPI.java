package com.qq.oldkingok.okapi.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OkConfigAPI {
    private final Map<String, OkConfig> configMap = new HashMap<>();
    private final Plugin plugin;
    public OkConfigAPI(Plugin plugin) {this.plugin = plugin;}

    public void load(@NotNull String... configs) {
        for (String config : configs) {
            plugin.saveResource(config,false);

            File dataFile = new File(plugin.getDataFolder(), config);
            FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);

            configMap.put(config, new OkConfig(config, dataConfig, dataFile));
        }
    }

    public OkConfig getOkConfig(String configStr){
        OkConfig config = configMap.get(configStr);
        if (config == null) throw new IllegalArgumentException("Could not find configuration: " + configStr);
        return config;
    }
}
