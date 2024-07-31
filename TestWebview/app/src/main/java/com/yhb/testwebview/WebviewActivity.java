package com.yhb.testwebview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

/**客户端*/
public class WebviewActivity extends AppCompatActivity {

    /**webFragment*/
    private ApWebviewFragment webviewFragment;

    /**创建*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webviewFragment = new ApWebviewFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl, webviewFragment).commit();
    }

    /**网页返回拦截虚拟返回键*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return webviewFragment.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**网页刷新*/
    private void reload(){
        webviewFragment.apWebview().reload();
    }

    /**释放*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

}