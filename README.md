# 子命令API
在主类enable任意位置调用
`OkCmdAPI okCmdApi = new OkCmdAPI()`，

## 注册

新建一个类，用来处理子命令

```java
public class SubCmdHandler implements OkCmdListener {
    @OkCmd(label = "toggle", description = "to toggle display")
    public boolean onToggle(CommandSender sender, String[] args) {
        // do something
        return true;
    }

    /**
     * 当玩家输入不带参数的主命令时，会调用此方法，
     * 如果此方法没有定义，就会调用默认的 helpCmd
     * 只能有一个help=true的方法
     */
    @OkCmd(label = "help", description = "help", help = true)
    public void onHelp(CommandSender sender) {
        // 在这里输出help信息
    }
}
```
然后 `OkCmdAPI::Register(class <? implements OkCmdListener>)`

## 调用

```java
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        okCmdAPI.invoke(sender, args);
        return true;
    }
```

# 配置文件API

在主类enable任意位置调用
`OkConfigAPI okConfigAPI = new OkConfigAPI()`，

## 保存默认配置文件

```java
okConfigAPI.load("config.yml", "data.yml");
```

## 获取OkConfig

```java
okConfigAPI.getOkConfig("config.yml");
```

### 编辑（自动保存）

```java

```