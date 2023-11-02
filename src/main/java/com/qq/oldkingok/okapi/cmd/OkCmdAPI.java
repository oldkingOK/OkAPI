package com.qq.oldkingok.okapi.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class OkCmdAPI {
    JavaPlugin plugin;
    private final Map<OkCmdListener, Set<Method>> listenerMethodMap = new HashMap<>();
    private OkCmdListener helpListener;
    private Method helpMethod;
    private String mainCmd;

    public OkCmdAPI(JavaPlugin plugin, String mainCmd) {
        this.plugin = plugin;
        this.mainCmd = mainCmd;
    }

    /**
     * 注册子命令处理器
     * @param listener
     */
    public void register(OkCmdListener listener) {
        Set<Method> methods = listenerMethodMap.computeIfAbsent(listener, k -> new HashSet<>());

        for (Method method : listener.getClass().getMethods()) {
            if (!method.isAnnotationPresent(OkCmd.class)) continue;
            OkCmd okCmd = method.getAnnotation(OkCmd.class);

            int paramsCount = method.getParameterCount();
            Parameter[] parameters = method.getParameters();

            /**
             * 如果是是帮助命令
             */
            if (okCmd.isHelp()) {
                if (paramsCount != 1 ||
                    CommandSender.class != parameters[0].getType()) {
                    throw new IllegalArgumentException("OkCmd help cmd {" + okCmd.label() +
                            "} register failed, parameters must be (CommandSender)");
                }

                helpListener = listener;
                helpMethod = method;
                continue;
            }

            if (method.getReturnType() != boolean.class) {
                throw new IllegalArgumentException("OkCmd {" + okCmd.label() +
                        "} register failed, return type must be boolean");
            }

            // 如果不需要args参数
            if (paramsCount == 1) {
                if (!CommandSender.class.isAssignableFrom(parameters[0].getType())) {
                    throw new IllegalArgumentException("OkCmd {" + okCmd.label() +
                            "} register failed, parameters must be (Class <? extends CommandSender>)");
                }
            }

            /*
             判断第一个参数是不是CommandSender的子类
             第二个是否是参数列表
             */
            if (paramsCount == 2 &&
                    !CommandSender.class.isAssignableFrom(parameters[0].getType()) ||
                    parameters[1].getType() != String[].class) {
                throw new IllegalArgumentException("OkCmd {" + okCmd.label() +
                        "} register failed, parameters must be (Class <? extends CommandSender>, String[])"
                );
            }

            if (paramsCount > 2) {
                throw new IllegalArgumentException("OkCmd {" + okCmd.label() +
                        "} register failed, parameters amount must be 1 or 2"
                );
            }

            methods.add(method);
        }
    }

    /**
     * 执行子命令
     * @param sender 命令发送者
     * @param originalArgs 从onCommand直接传下来的args，会自动切割
     */
    public void invoke(CommandSender sender, String[] originalArgs) {
        try {
            invokeInside(sender, originalArgs);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行子命令
     * @param sender 命令发送者
     * @param originalArgs 从onCommand直接传下来的args，会自动切割
     */
    private void invokeInside(CommandSender sender, String[] originalArgs) throws InvocationTargetException, IllegalAccessException {
        // 无参，输出help
        if (originalArgs.length == 0) {
            helpCmd(sender);
            return;
        }

        String label = originalArgs[0];
        // 切割参数列表
        String[] args = Arrays.copyOfRange(originalArgs, 1, originalArgs.length);

        // 执行方法
        for (OkCmdListener listener : listenerMethodMap.keySet()) {
            Set<Method> methods = listenerMethodMap.get(listener);

            for (Method method : methods) {
                OkCmd okCmd = method.getAnnotation(OkCmd.class);
                if (!okCmd.label().equalsIgnoreCase(label)) continue;
                // 检查玩家
                if (method.getParameters()[0].getType() == Player.class) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "You must be a Player!");
                        return;
                    }
                }
                // 检查参数
                boolean isSuccess = false;
                if (method.getParameterCount() == 1) {
                    isSuccess = (boolean) method.invoke(listener, sender);
                } else {
                    isSuccess = (boolean) method.invoke(listener, sender, args);
                }
                // 未成功就发送help
                if (!isSuccess) helpCmd(sender);
                return;
            }
        }

        // 能执行到这说明找不到方法
        helpCmd(sender);
    }

    public void helpCmd(CommandSender sender) throws InvocationTargetException, IllegalAccessException {
        if (helpMethod == null) {
            defaultHelp(sender);
            return;
        }

        helpMethod.invoke(helpListener, sender);
        return;
    }

    public void defaultHelp(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        sb.append("====");
        sb.append(plugin.getName());
        sb.append(" help ====");

        for (Set<Method> methods : listenerMethodMap.values()) {
            for (Method method : methods) {
                OkCmd okCmd = method.getAnnotation(OkCmd.class);
                sb.append("\n")
                        .append(mainCmd)
                        .append(" ")
                        .append(okCmd.label())
                        .append(" ")
                        .append(okCmd.description());
            }
        }

        sender.sendMessage(sb.toString());
    }
}
