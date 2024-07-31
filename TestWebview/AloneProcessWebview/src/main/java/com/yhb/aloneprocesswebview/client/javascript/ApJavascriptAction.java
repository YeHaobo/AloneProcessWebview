package com.yhb.aloneprocesswebview.client.javascript;

/**JS动作接口*/
public interface ApJavascriptAction {

    /**同步*/
    void syncExecute(String uuid, String action, String params, String callbackFunctionName);

    /**异步*/
    void asyncExecute(String uuid, String action, String params, String callbackFunctionName);

}