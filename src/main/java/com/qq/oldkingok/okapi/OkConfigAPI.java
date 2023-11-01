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
import java.util.List;
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


    @Builder
    static public class OkConfig{
        String name;
        FileConfiguration dataConfig;
        File dataFile;

        void save() {
            try {
                dataConfig.save(dataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void edit(ConfigRunnable runnable) {
            runnable.run(dataConfig);
            save();
        }

        /**
         * 获取String，自动替换&为颜色符号
         * @param node
         * @return
         */
        public String getStr(String node){
            String str = dataConfig.getString(node);
            if (str == null) throw new IllegalArgumentException(
                    "Could not find node: "+node+" in config: " + name);
            return str.replace('&', ChatColor.COLOR_CHAR);
        }

        public List<String> getStrList(String node){
            List<String> strs = dataConfig.getStringList(node);
            for (int i = 0; i < strs.size(); i++) {
                strs.set(i, strs.get(i).replace('&', ChatColor.COLOR_CHAR));
            }
            return strs;
        }

        public FileConfiguration getConfig(){
            return dataConfig;
        }
    }

    public abstract static class ConfigRunnable{
        protected abstract void run(FileConfiguration config);
    }
}
