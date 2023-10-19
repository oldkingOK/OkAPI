package com.qq.oldkingok.okapi;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class OkAPI extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "欢迎使用oldkingOK的API！");
    }
}
