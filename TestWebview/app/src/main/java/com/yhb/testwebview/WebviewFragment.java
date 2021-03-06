package com.yhb.testwebview;

import android.annotation.SuppressLint;
import com.yhb.aloneprocesswebview.view.ProWebview;
import com.yhb.aloneprocesswebview.view.BaseWebviewFragment;

public class WebviewFragment extends BaseWebviewFragment {
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_webview;
    }

    @Override
    public int getWebviewId() {
        return R.id.webview;
    }

    @Override
    public String getStartUrl() {
        return "file:///android_asset/aidl.html";
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public boolean initWebview(ProWebview webview) {
        /****其他初始化操作都在此做，这里只设置了开启的JS**/
        webview.getSettings().setJavaScriptEnabled(true);
        return true;
    }

    @Override
    public String getOnResumeActionName() {
        return "onWebResume";
    }

    @Override
    public String getOnPauseActionName() {
        return "onWebPause";
    }

    @Override
    public String getOnStopActionName() {
        return "onWebStop";
    }

    @Override
    public String getOnDestroyActionName() {
        return "onWebDestroy";
    }

}