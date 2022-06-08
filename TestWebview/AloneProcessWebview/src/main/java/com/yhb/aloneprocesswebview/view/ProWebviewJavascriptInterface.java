package com.yhb.aloneprocesswebview.view;

import android.os.Handler;
import android.webkit.JavascriptInterface;

/**webview 的 native接口*/
public class ProWebviewJavascriptInterface {
    private final Handler mHandler = new Handler();
    private JavascriptCommand javascriptCommand;

    @JavascriptInterface
    public void post(final String cmd, final String param){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                javascriptCommand.exec(cmd,param);
            }
        });
    }

    public void setJavascriptCommand(JavascriptCommand javascriptCommand) {
        this.javascriptCommand = javascriptCommand;
    }

    public interface JavascriptCommand {
        void exec(String cmd, String params);
    }

}