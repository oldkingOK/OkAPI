package com.qq.oldkingok.okapi;

import com.qq.oldkingok.okapi.cmd.OkCmdAPI;
import com.qq.oldkingok.okapi.test.CmdHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class OkAPI extends JavaPlugin {
    OkCmdAPI okCmdAPI;
    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "欢迎使用oldkingOK的API！");
        getCommand("oktest").setExecutor(this);
        okCmdAPI = new OkCmdAPI(this, "oktest");
        okCmdAPI.register(new CmdHandler());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        okCmdAPI.invoke(sender, args);
        return true;
    }
}
