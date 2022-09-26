package com.yhb.aloneprocesswebview.client.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.yhb.aloneprocesswebview.client.CommandDispatcher;

/**webview封装*/
public class ProWebview extends WebView implements JavascriptCommand{

    /**构造*/
    public ProWebview(Context context) {
        super(context);
        init();
    }
    public ProWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public ProWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**java native接口初始化*/
    private void init(){
        CommandDispatcher.getInstance().initAidlConnect(getContext());//初始化命令分发器，建立与主进程服务的连接
        addJavascriptInterface(new ProWebviewJavascriptInterface(this), "webview");//注入 JS
    }

    /**native接口收到命令*/
    @Override
    public void exec(String cmd, String params) {
        CommandDispatcher.getInstance().exec(cmd, params, this);//分发命令
    }

    /**加载JS（脚本）*/
    public void loadJsContent(String script){
        evaluateJavascript(script,null);
    }

    /**加载JS（无参）*/
    public void loadJsFunction(String functionName){
        String script = "javascript:" + functionName + "()";
        evaluateJavascript(script,null);
    }

    /**加载JS（有参）*/
    public void loadJsFunction(String functionName, String params){
        String script = "javascript:" + functionName + "('" + params + "')";
        evaluateJavascript(script,null);
    }

    /**加载html*/
    public void loadHtmlContent(String htmlContent){
        loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
    }

    /**销毁重写*/
    @Override
    public void destroy() {
        stopLoading();
        if(getHandler() != null){
            getHandler().removeCallbacksAndMessages(null);
        }
        removeAllViews();
        ((ViewGroup) getParent()).removeView(this);
        setWebChromeClient(null);
        setWebViewClient(null);
        setTag(null);
        clearHistory();
        super.destroy();
    }

}