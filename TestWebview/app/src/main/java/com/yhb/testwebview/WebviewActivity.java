package com.yhb.testwebview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;


/**客户端*/
public class WebviewActivity extends AppCompatActivity {

    private WebviewFragment webviewFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webviewFragment = new WebviewFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl, webviewFragment).commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //网页返回拦截虚拟返回键
        return webviewFragment.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    private void reload(){
        webviewFragment.getProWebview().reload();//网页刷新
    }

}