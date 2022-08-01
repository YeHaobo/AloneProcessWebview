package com.yhb.aloneprocesswebview.client.view;

import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;

/**webview 的 native接口*/
public class ProWebviewJavascriptInterface {
    private static final String TAG = "ProWebviewJavascriptInterface";

    /**主线程handler*/
    private Handler mainHandler;
    /**js回调接口*/
    private JavascriptCommand javascriptCommand;

    /**构造*/
    public ProWebviewJavascriptInterface(JavascriptCommand javascriptCommand) {
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.javascriptCommand = javascriptCommand;
    }

    /**Javascript调用java回调*/
    @JavascriptInterface
    public void post(final String cmd, final String param){
        //此处回调在JavaBridge线程需要转到主UI线程
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                javascriptCommand.exec(cmd, param);
            }
        });
    }

}