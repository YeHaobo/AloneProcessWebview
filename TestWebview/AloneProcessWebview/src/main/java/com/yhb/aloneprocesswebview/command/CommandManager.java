package com.yhb.aloneprocesswebview.command;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;

/**命令管理器*/
public class CommandManager {
    private static final String TAG = "CommandManager";

    /**单例*/
    private static CommandManager instance;
    public static CommandManager getInstance() {
        if (instance == null) {
            synchronized (CommandManager.class) {
                instance = new CommandManager();
            }
        }
        return instance;
    }

    /**命令Map*/
    private HashMap<String, Command> commandHashMap;

    /**构造*/
    private CommandManager() {
        this.commandHashMap = new HashMap<>();
    }

    /**注册命令*/
    public void registerCommand(Command command) {
        commandHashMap.put(command.name(), command);
    }

    /**查找所有命令*/
    public HashMap<String, Command> allCommand(){
        return commandHashMap;
    }

    /**查找命令*/
    public Command findCommand(String name){
        return commandHashMap.get(name);
    }

    /**执行命令*/
    public void execCommand(String name, Map params, CommandResultBack resultBack){
        Command command = commandHashMap.get(name);
        if(command != null){
            command.exec(params,resultBack);
        } else {
            Log.e(TAG,"undefind '" + name + "' command");
        }
    }

}