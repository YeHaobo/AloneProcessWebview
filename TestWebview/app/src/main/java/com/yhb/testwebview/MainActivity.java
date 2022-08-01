package com.yhb.testwebview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yhb.aloneprocesswebview.server.command.Command;
import com.yhb.aloneprocesswebview.server.command.CommandManager;

import java.util.concurrent.ConcurrentHashMap;

/**服务端*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /******************************服务端的命令注册*****************************/
        ToastCommand toastCommand = new ToastCommand(this);
        CommandManager.getInstance().registerCommand(toastCommand);//注册命令
//        CommandManager.getInstance().unregisterCommand(toastCommand);//解注册命令
//        CommandManager.getInstance().findCommand("toast");//查找命令
//        CommandManager.getInstance().clearCommand();//清除所有命令
//        ConcurrentHashMap<String, Command> map = CommandManager.getInstance().allCommand();//获取所有命令
        /********************************************************************/

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WebviewActivity.class);
                startActivity(intent);
            }
        });
    }

}