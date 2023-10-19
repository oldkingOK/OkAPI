package com.qq.oldkingok.okapi;

import lombok.Builder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OkConfigAPI {
    private final Map<String, OkConfig> configMap = new HashMap<>();
    private final Plugin plugin;
    OkConfigAPI(Plugin plugin) {this.plugin = plugin;}

    public void load(@NotNull String... configs) {
        for (String config : configs) {
            plugin.saveResource(config,false);

            File dataFile = new File(plugin.getDataFolder(), "data.yml");
            FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);

            configMap.put(config, new OkConfig(dataConfig, dataFile));
        }
    }

    public FileConfiguration getConfig(String configStr){
        return getOkConfig(configStr).dataConfig;
    }

    private OkConfig getOkConfig(String configStr){
        OkConfig config = configMap.get(configStr);
        if (config == null) throw new IllegalArgumentException("Could not find configuration: " + configStr);
        return config;
    }

    public String getStr(String configStr, String node){
        FileConfiguration config = getConfig(configStr);
        String str = config.getString(configStr);
        if (str == null) throw new IllegalArgumentException(
                "Could not find node: "+node+" in config: " + configStr);
        return str.replace('&', ChatColor.COLOR_CHAR);

    }

    public void edit(String configStr, ConfigRunnable runnable) {
        OkConfig config = getOkConfig(configStr);
        runnable.run(config.dataConfig);
        config.save();
    }

    @Builder
    static
    class OkConfig{
        FileConfiguration dataConfig;
        File dataFile;

        void save() {
            try {
                dataConfig.save(dataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract static class ConfigRunnable{
        protected abstract void run(FileConfiguration config);
    }
}
