package com.yhb.aloneprocesswebview.client.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.yhb.aloneprocesswebview.R;

/**承载webview的fragment*/
public abstract class BaseApWebviewFragment extends Fragment implements ApWebviewPrepareListener {

    /**初始化webview*/
    public abstract void initWebview(ApWebview apWebview);

    /**开始网址*/
    public abstract String getStartUrl();

    /**webview*/
    private ApWebview apWebview;

    /**获取webview*/
    public ApWebview apWebview(){
        return apWebview;
    }

    /**开始创建*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_webview, container, false);
        FrameLayout flParent = view.findViewById(R.id.fl_parent);
        apWebview = new ApWebview(view.getContext().getApplicationContext());
        apWebview.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        flParent.addView(apWebview);
        return view;
    }

    /**创建完成*/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apWebview.prepare(this);//webview准备
    }

    /**webview准备完成*/
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onPrepared() {
        initWebview(apWebview);//初始化webview
        if(!apWebview.getSettings().getJavaScriptEnabled()) apWebview.getSettings().setJavaScriptEnabled(true);//JS必须配置
        apWebview.loadUrl(getStartUrl());//加载链接
    }

    /**虚拟返回键 return true 不向下传递，return false 向下传递*/
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(apWebview != null && apWebview.canGoBack()){
                apWebview.goBack();
                return true;
            }
        }
        return false;
    }

    /**各生命周期回调的JS方法名*/
    public abstract String getOnResumeFunctionName();
    public abstract String getOnPauseFunctionName();
    public abstract String getOnStopFunctionName();
    public abstract String getOnDestroyFunctionName();

    /**各生命周期*/
    @Override
    public void onResume() {
        super.onResume();
        if(apWebview != null){
            apWebview.evaluateJavascript("javascript:" + getOnResumeFunctionName() + "()",null);
            apWebview.onResume();
        }
    }
    @Override
    public void onPause() {
        if(apWebview != null){
            apWebview.evaluateJavascript("javascript:" + getOnPauseFunctionName() + "()",null);
            apWebview.onPause();
        }
        super.onPause();
    }
    @Override
    public void onStop() {
        if(apWebview != null){
            apWebview.evaluateJavascript("javascript:" + getOnStopFunctionName() + "()",null);
        }
        super.onStop();
    }
    @Override
    public void onDestroyView() {
        if(apWebview != null){
            apWebview.evaluateJavascript("javascript:" + getOnDestroyFunctionName() + "()",null);
            apWebview.destroy();
            apWebview = null;
        }
        super.onDestroyView();
    }

}