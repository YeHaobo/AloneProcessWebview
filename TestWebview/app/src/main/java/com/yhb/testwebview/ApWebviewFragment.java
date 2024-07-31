package com.yhb.testwebview;

import android.annotation.SuppressLint;
import com.yhb.aloneprocesswebview.client.view.ApWebview;
import com.yhb.aloneprocesswebview.client.view.BaseApWebviewFragment;

public class ApWebviewFragment extends BaseApWebviewFragment {

    @Override
    public String getStartUrl() {
        return "file:///android_asset/aidl.html";
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initWebview(ApWebview webview) {
        webview.getSettings().setJavaScriptEnabled(true);
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