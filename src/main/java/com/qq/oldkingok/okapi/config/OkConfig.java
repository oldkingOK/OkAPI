package com.qq.oldkingok.okapi.config;

import lombok.Builder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Builder
public class OkConfig{
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

    public void edit(OkConfigRunnable runnable) {
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

    /**
     * 获取StringList，自动替换&为颜色符号
     * @param node
     * @return
     */
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