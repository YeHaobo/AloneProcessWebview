package com.yhb.aloneprocesswebview.server.command;

import android.util.Log;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**命令管理器*/
public class CommandManager {
    private static final String TAG = "CommandManager";

    /**单例*/
    private static CommandManager instance;
    public static CommandManager getInstance() {
        if (instance == null) {
            synchronized (CommandManager.class) {
                if(instance == null){
                    instance = new CommandManager();
                }
            }
        }
        return instance;
    }

    /**命令Map*/
    private ConcurrentHashMap<String, Command> commandHashMap;

    /**构造*/
    private CommandManager() {
        this.commandHashMap = new ConcurrentHashMap<>();
    }

    /**注册命令*/
    public void registerCommand(Command command) {
        commandHashMap.put(command.name(), command);
    }

    /**解除注册命令*/
    public void unregisterCommand(Command command) {
        commandHashMap.remove(command.name());
    }

    /**移除所有命令*/
    public void clearCommand(){
        commandHashMap.clear();
    }

    /**所有命令*/
    public ConcurrentHashMap<String, Command> allCommand(){
        return commandHashMap;
    }

    /**查找命令*/
    public Command findCommand(String name){
        return commandHashMap.get(name);
    }

    /**执行命令*/
    public void execCommand(String name, Map params, CommandResult resultBack){
        Command command = commandHashMap.get(name);
        if(command != null){
            command.exec(params,resultBack);
        } else {
            Log.e(TAG,"undefind '" + name + "' command");
        }
    }

}