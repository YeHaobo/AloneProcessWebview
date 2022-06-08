package com.yhb.aloneprocesswebview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.yhb.aloneprocesswebview.CommandDispatcher;

/**webview封装*/
public class ProWebview extends WebView implements ProWebviewJavascriptInterface.JavascriptCommand{

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
        ProWebviewJavascriptInterface proWebviewJavascriptInterface = new ProWebviewJavascriptInterface();//实例native接口
        proWebviewJavascriptInterface.setJavascriptCommand(this);//设置回调
        addJavascriptInterface(proWebviewJavascriptInterface, "webview");//注入 JS
    }

    /**native接口收到命令*/
    @Override
    public void exec(String cmd, String params) {
        CommandDispatcher.getInstance().exec(cmd, params, this);//分发命令
    }

    /**native接口执行命令回调*/
    public void handleCallback(final String action, final String params){
        loadJsFunction(action, params);//加载指定回调方法
    }

    /**加载JS（脚本）*/
    public void loadJsContent(String script){
        evaluateJavascript(script,null);
    }

    /**加载JS（无参）*/
    public void loadJsFunction(String action){
        String script = "javascript:" + action + "()";
        evaluateJavascript(script,null);
    }

    /**加载JS（有参）*/
    public void loadJsFunction(String action, String params){
        String script = "javascript:" + action + "('" + params + "')";
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
        getHandler().removeCallbacksAndMessages(null);
        removeAllViews();
        ((ViewGroup) getParent()).removeView(this);
        setWebChromeClient(null);
        setWebViewClient(null);
        setTag(null);
        clearHistory();
        super.destroy();
    }

}