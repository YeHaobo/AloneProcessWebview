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
        return "onWebResume";
    }

    @Override
    public String getOnPauseFunctionName() {
        return "onWebPause";
    }

    @Override
    public String getOnStopFunctionName() {
        return "onWebStop";
    }

    @Override
    public String getOnDestroyFunctionName() {
        return "onWebDestroy";
    }

}