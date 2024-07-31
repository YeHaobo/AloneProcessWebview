package com.yhb.aloneprocesswebview.client.javascript;

import android.webkit.JavascriptInterface;

/**js的native接口*/
public class ApJavascriptInterface {

    /**动作接口*/
    private ApJavascriptAction jsAction;

    /**构造*/
    public ApJavascriptInterface(ApJavascriptAction jsAction) {
        this.jsAction = jsAction;
    }

    /**Javascript调用（同步）*/
    @JavascriptInterface
    public void syncPost(String uuid, String action, String params, String callbackFunctionName){
        jsAction.syncExecute(uuid, action, params, callbackFunctionName);
    }

    /**Javascript调用（异步）*/
    @JavascriptInterface
    public void asyncPost(String uuid, String action, String params, String callbackFunctionName){
        jsAction.asyncExecute(uuid, action, params, callbackFunctionName);
    }

}