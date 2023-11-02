package com.qq.oldkingok.okapi.config;

import org.bukkit.configuration.file.FileConfiguration;

@FunctionalInterface
public interface OkConfigRunnable {
    public void run(FileConfiguration config);
}
