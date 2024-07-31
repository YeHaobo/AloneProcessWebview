package com.yhb.testwebview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.yhb.aloneprocesswebview.server.action.ApWebviewAction;
import com.yhb.aloneprocesswebview.server.action.ApWebviewActionManager;
import java.util.concurrent.ConcurrentHashMap;

/**入口*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApWebviewActionManager.getInstance().register(new AppInfoAction());//注册命令
//        ApWebviewActionManager.getInstance().unregister("appInfo");//解注册命令
//        ApWebviewActionManager.getInstance().find("appInfo");//查找命令
//        ApWebviewActionManager.getInstance().clear();//清除所有命令
//        ConcurrentHashMap<String, ApWebviewAction> map = ApWebviewActionManager.getInstance().all();//获取所有命令

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WebviewActivity.class));
            }
        });

    }

}