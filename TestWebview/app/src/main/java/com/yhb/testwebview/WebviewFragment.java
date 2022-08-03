package com.yhb.testwebview;

import com.yhb.aloneprocesswebview.client.view.ProWebview;
import com.yhb.aloneprocesswebview.client.view.BaseWebviewFragment;

public class WebviewFragment extends BaseWebviewFragment {

    @Override
    public String getStartUrl() {
        return "file:///android_asset/aidl.html";
    }

    @Override
    public boolean initWebview(ProWebview webview) {
//        webview.getSettings().setJavaScriptEnabled(true);
        return false;//默认返回false，只开启JS支持
    }

    @Override
    public String getOnResumeFunctionName() {
        return "onWebResume";//当fragment进入OnResume时会调用JS中onWebResume方法
    }

    @Override
    public String getOnPauseFunctionName() {
        return "onWebPause";//当fragment进入OnPause时会调用JS中onWebPause方法
    }

    @Override
    public String getOnStopFunctionName() {
        return "onWebStop";//当fragment进入OnStop时会调用JS中onWebStop方法
    }

    @Override
    public String getOnDestroyFunctionName() {
        return "onWebDestroy";//当fragment进入OnDestroy时会调用JS中onWebDestroy方法
    }

}