package com.yhb.aloneprocesswebview.server.action;

import android.util.Log;
import java.util.concurrent.ConcurrentHashMap;

/**动作管理器*/
public class ApWebviewActionManager {

    /**TAG*/
    private static final String TAG = "ApWebviewActionManager";

    /**单例模式*/
    private static ApWebviewActionManager instance;
    public static ApWebviewActionManager getInstance() {
        if (instance == null) {
            synchronized (ApWebviewActionManager.class) {
                if(instance == null){
                    instance = new ApWebviewActionManager();
                }
            }
        }
        return instance;
    }

    /**存储动作 Map（供服务端使用，ConcurrentHashMap线程安全）*/
    private ConcurrentHashMap<String, ApWebviewAction> actionHashMap;

    /**构造*/
    private ApWebviewActionManager() {
        this.actionHashMap = new ConcurrentHashMap<>();
    }

    /**注册*/
    public void register(ApWebviewAction action) {
        actionHashMap.put(action.name(), action);
    }

    /**解注册*/
    public void unregister(String name) {
        actionHashMap.remove(name);
    }

    /**所有*/
    public ConcurrentHashMap<String, ApWebviewAction> all(){
        return actionHashMap;
    }

    /**查找*/
    public ApWebviewAction find(String name){
        return actionHashMap.get(name);
    }

    /**清空*/
    public void clear(){
        actionHashMap.clear();
    }

    /**执行*/
    public void execute(String name, String params, ApWebviewActionResult result){
        ApWebviewAction action = find(name);
        if(action == null){
            Log.e(TAG, "not find webview action '" + name + "'");
        } else{
            action.execute(params, result);
        }
    }

}