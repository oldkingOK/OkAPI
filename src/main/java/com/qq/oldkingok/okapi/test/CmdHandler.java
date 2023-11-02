package com.qq.oldkingok.okapi.test;

import com.qq.oldkingok.okapi.cmd.OkCmd;
import com.qq.oldkingok.okapi.cmd.OkCmdListener;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CmdHandler implements OkCmdListener,Listener {
    @OkCmd(label = "help", description = "插件帮助", isHelp = true)
    public boolean onHelp(CommandSender sender) {
        sender.sendMessage("这是插件帮助");
        return true;
    }

    @OkCmd(label = "test", description = "玩家测试测试")
    public boolean onPlayerTest(Player player) {
        player.sendMessage("恭喜你完成玩家测试测试");
        return true;
    }
}
