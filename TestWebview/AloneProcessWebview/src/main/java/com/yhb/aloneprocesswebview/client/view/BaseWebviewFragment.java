package com.yhb.aloneprocesswebview.client.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.yhb.aloneprocesswebview.R;

/**承载webview的fragment*/
public abstract class BaseWebviewFragment extends Fragment {

    /**onResume时执行的JS的方法名*/
    public abstract String getOnResumeFunctionName();

    /**onPause时执行的JS的方法名*/
    public abstract String getOnPauseFunctionName();

    /**onStop时执行的JS的方法名*/
    public abstract String getOnStopFunctionName();

    /**onDestroy时执行的JS的方法名*/
    public abstract String getOnDestroyFunctionName();

    /**初始化webview，return true 已初始化，return false 未初始化*/
    public abstract boolean initWebview(ProWebview proWebview);

    /**开始网址*/
    public abstract String getStartUrl();

    /**webview*/
    private ProWebview proWebview;

    /**获取webview*/
    public ProWebview getProWebview(){
        return proWebview;
    }

    /**开始创建*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_webview, container, false);
        FrameLayout flParent = view.findViewById(R.id.fl_parent);
        proWebview = new ProWebview(view.getContext().getApplicationContext());//使用ApplicationContext
        proWebview.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        flParent.addView(proWebview);//动态添加webview
        return view;
    }

    /**创建完成*/
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化webview
        boolean isInit = initWebview(proWebview);
        if(!isInit)proWebview.getSettings().setJavaScriptEnabled(true);//打开JS
        //加载初始url
        String startUrl = getStartUrl();
        if(!TextUtils.isEmpty(startUrl))proWebview.loadUrl(startUrl);
    }

    /**虚拟返回键 return true 不向下传递，return false 向下传递*/
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(proWebview != null && proWebview.canGoBack()){
                proWebview.goBack();
                return true;
            }
        }
        return false;
    }

    /***************以下是生命周期控制 和 回调 JS*****************/

    @Override
    public void onResume() {
        super.onResume();
        if(proWebview != null){
            proWebview.loadJsFunction(getOnResumeFunctionName());
            proWebview.onResume();
        }
    }

    @Override
    public void onPause() {
        if(proWebview != null){
            proWebview.loadJsFunction(getOnPauseFunctionName());
            proWebview.onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if(proWebview != null){
            proWebview.loadJsFunction(getOnStopFunctionName());
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if(proWebview != null){
            proWebview.loadJsFunction(getOnDestroyFunctionName());
            proWebview.destroy();
            proWebview = null;
        }
        super.onDestroyView();
    }

}